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

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various File utilities to deal with URLs.
 *
 * @version 0.2
 */
public class FileUtilities {
   private static final Pattern PAT = Pattern.compile("(.*)[/\\x5C]\\w+[/\\x5C]\\x2E\\x2E[/\\x5C](.*)");
   private static final Pattern PAT2 = Pattern.compile("(.*)[/\\x5C]\\w+[/\\x5C]\\x2E\\x2E");

   private FileUtilities() {
   }

   /**
    * Collapse an File to a representation without any <code>..</code> constructs.
    *
    * @param file the File
    * @return the collapsed file representation
    */
   private static File collapse(File file) {
      String path = file.getAbsolutePath();
      while (true) {
         Matcher m = PAT.matcher(path);
         if (m.matches()) {
            path = m.group(1) + "/" + m.group(2);
         } else {
            m = PAT2.matcher(path);
            if (m.matches()) {
               path = m.group(1);
            } else {
               break;
            }
         }
      }
      return new File(path);
   }

   /**
    * Return the File whose name is defined by path, and which may be defined relative to a parent directory.
    *
    * @param dir the base directory
    * @param path the path
    * @return the File
    */
   public static File getFile(File dir, String path) {
      if (path == null) {
         return null;
      } else {
         File file = new File(path);
         if (!file.isAbsolute()) {
            file = new File(dir, path);
            file = collapse(file);
         }
         return file;
      }
   }
}
