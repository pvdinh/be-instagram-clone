package com.example.demo.repository;

import com.example.demo.models.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {
    List<Message> findMessagesBySender(String sender);
    List<Message> findMessagesBySenderAndReceiver(String sender,String receiver);
}
