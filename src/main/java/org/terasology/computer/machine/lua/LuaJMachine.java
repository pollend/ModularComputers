/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.computer.machine.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.terasology.computer.machine.Computer;
import org.terasology.computer.machine.Machine;
import org.terasology.computer.module.BaseComputerModule;
import org.terasology.computer.module.ComputerMethod;
import org.terasology.computer.module.RegisterComputerModule;
import org.terasology.computer.system.ModuleRegistrySystem;
import sun.misc.Launcher;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class LuaJMachine implements Machine{
    private static Globals serverGlobals;

    public LuaJMachine(){
        if(serverGlobals == null)
        {
            serverGlobals = new Globals();
            serverGlobals.load(new JseBaseLib());
            serverGlobals.load(new PackageLib());
            serverGlobals.load(new StringLib());

            LoadState.install(serverGlobals);
            LuaC.install(serverGlobals);
            LuaString.s_metatable = new ReadOnlyLuaTable(LuaString.s_metatable);
        }

    }



    public LuaTask run(Computer owning,String command, InputStream stream, Iterable<BaseComputerModule> modules)
    {
        Globals userGlobal = new Globals();

        userGlobal.load(new JseBaseLib());
        userGlobal.load(new TableLib());
        userGlobal.load(new StringLib());
        userGlobal.load(new JseMathLib());

        // However we don't wish to expose the library in the user globals,
        // so it is immediately removed from the user globals once created.
        userGlobal.load(new DebugLib());
        LuaValue sethook = userGlobal.get("debug").get("sethook");
        userGlobal.set("debug",LuaValue.NIL);

        for(BaseComputerModule module: modules)
        {
            LuaTable table = new LuaTable();
            RegisterComputerModule info = ModuleRegistrySystem.getModuleInfo(module);
            Set<Method> methods = ModuleRegistrySystem.getMethods(module);
            for(Method method : methods)
            {
                ComputerMethod methodInfo = ModuleRegistrySystem.getMethodInfo(method);
                table.set(methodInfo.name(),new ModuleFunction(module,method,this));
            }
            userGlobal.set(info.name(),table);

        }

        LuaValue chunk = serverGlobals.load(stream, "main","t", userGlobal);
        LuaThread thread = new LuaThread(userGlobal, chunk);


//        LuaValue hookfunc = new ZeroArgFunction() {
//            public LuaValue call() {
//                thread.
//                // A simple lua error may be caught by the script, but a
//                // Java Error will pass through to top and stop the script.
//                throw new Error("Script overran resource limits.");
//            }
//        };
//        final int instruction_count = 20;
//        sethook.invoke(LuaValue.varargsOf(new LuaValue[] { thread, hookfunc, LuaValue.EMPTYSTRING, LuaValue.valueOf(instruction_count) }));

        thread.resume(LuaValue.NIL);
        return new LuaTask(command,thread);
    }


    public LuaValue toLuaValue(Object object) {
        if (object == null) {
            return LuaValue.NIL;
        } else if (object instanceof String) {
            String s = object.toString();
            return LuaValue.valueOf(s);
        } else if (object instanceof Boolean) {
            boolean bool = (boolean) object;
            return LuaValue.valueOf(bool);
        } else if (object instanceof Class && ((Class) object).isPrimitive()) {
            if (Integer.class.isInstance(object)) {
                return LuaValue.valueOf((int) object);
            } else if (Float.class.isInstance(object)) {
                return LuaValue.valueOf((float) object);
            } else if(Double.class.isInstance(object)) {
                return LuaValue.valueOf((double) object);
            }
            //throw exception can't process primative
        }
        return LuaValue.NIL;
    }


    public Object toObject(LuaValue luaValue)
    {
        switch (luaValue.type()) {
            case LuaValue.TNIL:
            case LuaValue.TNONE:
                    return null;
            case LuaValue.TBOOLEAN:
                return  luaValue.toboolean();
            case LuaValue.TNUMBER:
                return  luaValue.tofloat();
            case LuaValue.TINT:
                return  luaValue.toint();
            case LuaValue.TSTRING:
                return luaValue.toString();
            case LuaValue.TTABLE:
                //table support later
                return null;
            default:
                return null;

        }
    }


    public static class ModuleFunction extends VarArgFunction{
        private Method method;
        private LuaJMachine luaJMachine;
        private BaseComputerModule module;
        ModuleFunction(BaseComputerModule module,Method method,LuaJMachine luaJMachine) {
            this.method = method;
            this.luaJMachine = luaJMachine;
            this.module = module;
        }

        @Override
        public Varargs invoke(Varargs args) {

            Object[] result = new Object[args.narg()];
            for (int x = 0; x < args.narg(); x++) {
                result[x] = luaJMachine.toObject(args.arg(x));
            }
            try {
                return luaJMachine.toLuaValue(method.invoke(module, result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return super.invoke(args);
        }
    }


}
