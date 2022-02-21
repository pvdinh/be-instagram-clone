package com.example.demo.controllers.webSocketController;

import com.example.demo.models.Comment;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import com.example.demo.services.UserAccountService;
import com.example.demo.utils.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class PostWebSocketController {
    @Autowired
    private CommentService commentService;

    @MessageMapping("comment.allComment")
    @SendTo("/post/allComment")
    public Comment receiverComment(@Payload HashMap<Object, String> hashMap) {
        try {
            Comment comment = new Comment(hashMap.get("id"), hashMap.get("content"), hashMap.get("idPost"), hashMap.get("idUser"), Long.parseLong(hashMap.get("dateCommented")));
            if (comment.getIdPost() != null && comment.getIdUser() != null) {
                commentService.addCommentInPost(comment, TokenAuthentication.getUsernameFromToken(hashMap.get("sessionToken")));
            }
            return comment;
        }catch (Exception e){
            return new Comment();
        }
    }

    @MessageMapping("comment.deleteComment")
    @SendTo("/post/allComment")
    public Comment deleteComment(@Payload HashMap<Object, String> hashMap) {
        try {
            Comment comment = new Comment(hashMap.get("id"), hashMap.get("content"), hashMap.get("idPost"), hashMap.get("idUser"), Long.parseLong(hashMap.get("dateCommented")));
            if (comment.getIdPost() != null && comment.getIdUser() != null) {
                commentService.delete(comment, TokenAuthentication.getUsernameFromToken(hashMap.get("sessionToken")));
            }
            return comment;
        }catch (Exception e){
            return new Comment();
        }
    }
}
