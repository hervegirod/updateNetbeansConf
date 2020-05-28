/*
Copyright (c) 2020 Herve Girod
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
Alternatively if you have any questions about this project, you can visit
the project website at the project page on https://github.com/hervegirod/ChangeLicenceTag
 */
package org.girod.updatenbconf;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has only one static method which allows to return the launch arguments of a
 * main static method into a Map of &lt;key,value&gt;.
 *
 * @version 0.2
 */
public class LauncherUtils {
   private LauncherUtils() {
   }

   /**
    * Return the launch arguments of a main static method into a Map of &lt;key,value&gt;.
    * The arguments can have eone of the following pattern:
    * <pre>
    * key1 prop1 key2 prop2 ...
    * </pre>
    * or
    * <pre>
    * key1=prop1 key2=prop2 ...
    * </pre>
    * it is even possible to mix the two patterns such as:
    * <pre>
    * key1 prop1 key2=prop2 ...
    * </pre>
    *
    * if the value for the key is not set, then a (key, value) will be added with an empty String as value
    * <pre>
    * key2= -key3 -key4=value4
    * </pre>
    *
    * @param args the launch arguments
    * @return the key, value pairs corresponding to the arguments
    */
   public static Map<String, String> getLaunchProperties(String[] args) {
      Map<String, String> props = new HashMap<>();
      if ((args != null) && (args.length != 0)) {
         int i = 0;
         boolean toStore = false;
         String propKey = null;
         while (i < args.length) {
            String arg = args[i];
            if (toStore) {
               if (arg.charAt(0) != '-') {
                  toStore = false;
                  String propValue = arg;
                  props.put(propKey, propValue);
                  propKey = null;
               } else {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
            } else if (arg.indexOf('=') != -1) {
               if (toStore) {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
               String intKey;
               int eqIndex = arg.indexOf('=');
               if (eqIndex == -1) {
                  props.put(arg, "");
               } else {
                  intKey = arg.substring(0, eqIndex);
                  if (arg.indexOf('=') == arg.length() - 1) {
                     props.put(intKey, "");
                  } else {
                     String propValue = arg.substring(eqIndex + 1);
                     props.put(intKey, propValue);
                  }
               }
            } else if (arg.charAt(0) == '-') {
               if (toStore) {
                  toStore = false;
                  props.put(propKey, "");
                  propKey = null;
               }
               props.put(arg, "");
            } else if (arg.charAt(0) != '-') {
               propKey = arg;
               toStore = true;
            }
            i++;
         }
         if (propKey != null) {
            props.put(propKey, "");
         }
      }
      return props;
   }
}
