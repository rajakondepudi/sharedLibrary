def call(String dockerPrivateRegistry, String imageName, String buildImageTag, String imageMetadata, List images) 
{
    
        docker.withRegistry("http://${dockerPrivateRegistry}") 
		{
            def ctr = 0
            images.each 
			{
                println "Publishing Docker image for ${imageMetadata[ctr]['type']} named: ${imageMetadata[ctr]['imageSrcDirectory']}."
                it.push()
                imageName = (imageMetadata[ctr]['imageSrcDirectory']).replace("_","-")
                def out = sh script: "docker rmi ${dockerPrivateRegistry}/${componentToBuild}/${imageName}:${buildImageTag}", returnStdout: true
                ctr = ctr + 1
            }
        }
    
}
