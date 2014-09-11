/*
 *  Copyright (C) 2014 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.jsoftbiz.utils.OS;
import org.junit.Test;

/**
 *
 *
 */
@Slf4j
public class FingerprintTestCase
    {

    @Test
    public void test000()
        {
        log.debug("ping");

        log.debug("Java VM vendor  [{}]", System.getProperty("java.vm.vendor"));
        log.debug("Java VM name    [{}]", System.getProperty("java.vm.name"));
        log.debug("Java VM version [{}]", System.getProperty("java.vm.version"));

        log.debug("Java RE vendor  [{}]", System.getProperty("java.vendor"));
        log.debug("Java RE version [{}]", System.getProperty("java.version"));
        
        log.debug("OS name    [{}]", System.getProperty("os.name"));
        log.debug("OS arch    [{}]", System.getProperty("os.arch"));
        log.debug("OS version [{}]", System.getProperty("os.version"));
/*        
        log.debug("class   [{}]", this.getClass().getName());
        log.debug("package [{}]", this.getClass().getPackage().getName());
        log.debug("version [{}]", this.getClass().getPackage().getImplementationVersion());
        
        log.debug("Classpath [{}]", System.getProperty("java.class.path"));
        log.debug("Classpath [{}]", System.getProperty("path.separator"));

        for (String jarpath : System.getProperty("java.class.path").split(System.getProperty("path.separator")) )
            {
            try {
                if (jarpath.endsWith(".jar"))
                    {
                    File file= new File(jarpath);
                    log.debug("File    [{}]", file.getName());
                    JarFile jarfile = new JarFile(jarpath);
                    Manifest manifest = jarfile.getManifest();
                    log.debug("  Title   [{}]", manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE));
                    log.debug("  Vendor  [{}]", manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR));
                    log.debug("  Version [{}]", manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION));
                    }
                }
            catch (Exception ouch)
                {
                
                }
            }
 */
        Properties props = new Properties();
        try {
            props.load(
                this.getClass().getResourceAsStream(
                    "/firethorn-build.properties"
                    )
                );
            }
        catch (Exception ouch)
            {
            } 
        for (Object key : props.keySet())
            {
            log.debug(" [{}][{}]", key, props.get(key));
            }
        }

    @Test
    public void test001()
        {
        File dir = new File("/etc/");
        File fileList[] = new File[0];
        if(dir.exists())
            {
            fileList =  dir.listFiles(
                new FilenameFilter()
                    {
                    public boolean accept(File dir, String filename)
                        {
                        return filename.endsWith("-release");
                        }
                    }
                );
            }
        //looks for the version file (not all linux distros)
        File fileVersion = new File("/proc/version");
        if(fileVersion.exists())
            {
            fileList = Arrays.copyOf(fileList,fileList.length+1);
            fileList[fileList.length-1] = fileVersion;
            }       
        //prints all the version-related files
        for (File f : fileList) {
            try {
            log.debug("File [{}]", f);
                BufferedReader myReader = new BufferedReader(new FileReader(f));
                String strLine = null;
                while ((strLine = myReader.readLine()) != null)
                    {
                    log.debug("Version [{}]", strLine);
                    }
                myReader.close();
                }
            catch (Exception e)
                {
                //System.err.println("Error: " + e.getMessage());
                }
            }        
        }

    @Test
    public void test002()
        {
        OS os = OS.getOs();
        
        log.debug("name [{}]", os.getName());
        log.debug("version [{}]", os.getVersion());
        log.debug("platform [{}]", os.getPlatformName());

        }
    }
