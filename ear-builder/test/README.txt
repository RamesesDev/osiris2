This builder is used for building the enterprise and web templates
for osiris2. 

Before starting, ensure that the following is done.

1. apache ant is downloaded. 
2. ANT_HOME environment variable is pointed to the apache ant's folder 
3. JAVA_HOME is pointed to java
4. PATH environment (for Windows) includes the ant bin folder.


If items 2 down is not yet done, follow the suggested path if you're working with Windows:
1. Go to environment variables - in xp its located in My Computer. Go to Advanced Tab.
   Click on Environment Variables. Add in the System variables the ff:
   
   1. Add ANT_HOME = directory where apache ant is located
   2. Edit path and append the following lines
         ;%ANT_HOME%/bin
         
         
   3. Check also there is a JAVA_HOME which points to the java folder.     
   4. To test, launch cmd and type ant. The following should be displayed:
       Buildfile: build.xml does not exist!
       Build failed.     

   5. Ant is now ready.

