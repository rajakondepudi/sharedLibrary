def call(String sourceOrBinaries) 
{
  def hostdir = "/var/lib/docker/volumes/aiml-ci-cd_jenkins-data/_data"
  println "hostdir 1: ${hostdir}"
  hostdir = "${hostdir}${env.WORKSPACE}"
  println "hostdir 2: ${hostdir}"
  hostdir = hostdir -  "/var/jenkins_home"
  println "hostdir 3: ${hostdir}"

  def dockerRunCmd = "docker run -v ${hostdir}:/workspaceDir --rm python_pipeline_tools:v3.7.4 /workspaceDir/build/scripts/generateCythonModulesForPyx374.sh /workspaceDir"
  println "dockerRunCmd: ${dockerRunCmd}"
  sh script: "${dockerRunCmd}"

  if(sourceOrBinaries.equals("Cython Extension Modules")) 
  {
  
      dockerRunCmd = "docker run -v ${hostdir}:/workspaceDir --rm python_pipeline_tools:v3.7.4 /workspaceDir/build/scripts/generateCythonModules374.sh /workspaceDir"
      println "dockerRunCmd: ${dockerRunCmd}"
      sh script: "${dockerRunCmd}"
  }
}
