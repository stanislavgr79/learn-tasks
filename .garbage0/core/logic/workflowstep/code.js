function check(){
         var pageCurrent = workflowData.getPayload().toString();
         var session = workflowSession.getSession();
         var item = workflowSession.getSession().getItem(pageCurrent);
         var jcrNode = item.getSession().getNode(pageCurrent+"/jcr:content");

         if (jcrNode.hasProperty("pathToMove")){
               var destPath = jcrNode.getProperty("pathToMove").getString();
               if (!destPath.isEmpty() && session.getRootNode().getPath().equals("/") && session.getRootNode().hasNode(destPath.substring(1))){
               return true;
               }
         }
return false;
}