
function check(){

         var pageCurrent = workflowData.getPayload().toString();
         log.info('error3');
         log.info(pageCurrent);

         var item = workflowSession.getSession().getItem(pageCurrent);
         log.info('error4');
         log.info(item);

        if (item.isNode()){
         log.info('error6');
         log.info(item.isNode());
        var jcrNode = item.getSession().getNode(pageCurrent+"/jcr:content");
            log.info('error7');
            log.info(jcrNode);
             if (jcrNode.hasProperty("pathToMove")){
                 var destPath = jcrNode.getProperty("pathToMove").getString();
                 if (!destPath.isEmpty() && session.getRootNode().getPath().equals("/") && session.getRootNode().hasNode(destPath.substring(1))){

                 return true;
                 }
             return false;
             }
        }
     return false;
}

//function check() {
//    log.info('branch 1');
//    var path = workflowData.getPayload().toString();
//    var jcrsession = graniteWorkflowSession.adaptTo(Packages.javax.jcr.Session);
//    try{
//        var node = jcrsession.getNode(path).getNode("jcr:content");
//        if (!node.hasProperty("pathToMove")) {
//            log.info("Branche 1: pathToMove not exist. Need to call Dialog Participant");
//            return true;
//        } else{
//            log.info("Branche 1: pathToMove exist. Branche 1 will not be executed");
//            return false;
//        }
//    }catch(err) {
//         log.info(err.message);
//         return false;
//    }
//}
//
//function check() {
//    log.info('branch 2');
//    var path = workflowData.getPayload().toString();
//    var jcrsession = graniteWorkflowSession.adaptTo(Packages.javax.jcr.Session);
//    try{
//        var node = jcrsession.getNode(path).getNode("jcr:content");
//        if (node.hasProperty("pathToMove")) {
//            log.info("Branche 2: pathToMove exist. No Operation");
//            return true;
//        } else{
//            log.info("Branche 2: pathToMove not exist. Branche 2 will not be executed");
//            return false;
//        }
//    }catch(err) {
//         log.info(err.message);
//         return false;
//    }
//}