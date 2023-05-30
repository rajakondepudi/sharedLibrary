def call() 
{
            docker.withRegistry("http://${dockerPrivateRegistry}") 
			{
              imageMetadata.each 
			  {
                someImageMetadata = it
                imageSrcDirectory = someImageMetadata['imageSrcDirectory']
                imageName = imageSrcDirectory.replace("_","-")
                imageType = someImageMetadata['type']
                println "Building Docker image for image named: ${imageName}."
                images.add(docker.build("${componentToBuild}/${imageName}:${buildImageTag}","-f build/docker/images/${imageType}/${imageSrcDirectory}/Dockerfile ."))
              }
            }
}
