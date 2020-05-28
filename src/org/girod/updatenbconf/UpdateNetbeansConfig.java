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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 0.2
 */
public class UpdateNetbeansConfig {
   private static final String USER_DIR = "netbeans_default_userdir";
   private static final String CACHE_DIR = "netbeans_default_cachedir";
   private static final String JDK_HOME = "netbeans_jdkhome";
   private File confFile = null;

   public boolean apply(File userDir, Map<String, String> properties) {
      File nbInstall = null;
      File userCacheDir = null;
      File cacheDir = null;
      File jdk = null;
      if (properties.containsKey("install")) {
         nbInstall = FileUtilities.getFile(userDir, properties.get("install"));
         if (!nbInstall.exists() || !nbInstall.isDirectory()) {
            nbInstall = null;
         }
      }
      if (properties.containsKey("cache")) {
         System.out.println(userDir + ": " + properties.get("cache"));
         File cache = FileUtilities.getFile(userDir, properties.get("cache"));
         if (!cache.exists()) {
            cache.mkdir();
         }
         userCacheDir = new File(cache, "userDir");
         if (!userCacheDir.exists()) {
            userCacheDir.mkdir();
         }
         cacheDir = new File(cache, "cacheDir");
         if (!cacheDir.exists()) {
            cacheDir.mkdir();
         }
      }
      if (properties.containsKey("jdk")) {
         jdk = FileUtilities.getFile(userDir, properties.get("jdk"));
         if (!jdk.exists() || !jdk.isDirectory()) {
            jdk = null;
         }
      }
      if (jdk == null || cacheDir == null || userCacheDir == null || nbInstall == null) {
         System.err.println("Configuration incorrect");
         return false;
      }
      confFile = new File(nbInstall, "etc/netbeans.conf");
      if (!confFile.exists() || confFile.isDirectory()) {
         System.err.println("Configuration incorrect");
         return false;
      }

      try {
         applyImpl(jdk, userCacheDir, cacheDir);
      } catch (IOException e) {
         System.err.println("Unable to update etc/netbeans.conf");
         return false;
      }
      return true;
   }

   private void applyImpl(File jdk, File userCacheDir, File cacheDir) throws IOException {
      List<String> lines = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new FileReader(confFile))) {
         while (true) {
            String line = reader.readLine();
            if (line == null) {
               break;
            } else {
               lines.add(line);
            }
         }
      }
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(confFile))) {
         boolean isFirst = true;
         Iterator<String> it = lines.iterator();
         while (it.hasNext()) {
            String line = it.next();
            if (isFirst) {
               isFirst = false;
            } else {
               writer.newLine();
            }
            if (line.startsWith(USER_DIR) || line.startsWith("#" + USER_DIR)) {
               line = USER_DIR + "=\"" + userCacheDir.getAbsolutePath() + "\"";
            } else if (line.startsWith(CACHE_DIR) || line.startsWith("#" + CACHE_DIR)) {
               line = CACHE_DIR + "=\"" + cacheDir.getAbsolutePath() + "\"";
            } else if (line.startsWith(JDK_HOME) || line.startsWith("#" + JDK_HOME)) {
               line = JDK_HOME + "=\"" + jdk.getAbsolutePath() + "\"";
            }
            writer.append(line);
         }
         writer.flush();
      }
   }

   public static void main(String[] args) {
      File userDir = new File(System.getProperty("user.dir"));
      Map<String, String> properties = LauncherUtils.getLaunchProperties(args);
      UpdateNetbeansConfig config = new UpdateNetbeansConfig();
      boolean applied = config.apply(userDir, properties);
      if (!applied) {
         System.err.println("=> Nothing done");
      } else {
         System.out.println("=> File " + config.confFile + " updated");
      }
   }
}
