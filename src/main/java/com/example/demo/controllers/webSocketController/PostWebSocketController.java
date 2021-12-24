package com.example.demo.controllers.webSocketController;

import com.example.demo.models.Comment;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import com.example.demo.services.UserAccountService;
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
    public Comment receiverComment(@Payload Comment comment){
        commentService.addCommentInPost(comment);
        return comment;
    }
}
