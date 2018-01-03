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
package org.terasology.computer.module;

import org.terasology.computer.machine.Computer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public @interface ComputerCommand {
    String name();
    String description();

   class ComputerCommandInfo {
        private BaseComputerModule module;
        private String name;
        private Method method;

        public ComputerCommandInfo(BaseComputerModule module, Method method) {
            this.module = module;
            this.method = method;
            this.name = name;
        }

        public Method getMethod() {
            return method;
        }

        public BaseComputerModule getModule() {
            return module;
        }

        public boolean invoke(Computer computer, String[] args) throws InvocationTargetException, IllegalAccessException {
            Object[] result = new Object[args.length + 1];
            result[0] = computer;
            for (int x = 1; x < args.length + 1; x++)
                result[x] = args[x - 1];
            return (boolean) method.invoke(module, result);
        }

        public ComputerCommand getInfoMeta() {
            return method.getAnnotation(ComputerCommand.class);
        }
    }
}
