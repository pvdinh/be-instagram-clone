package com.example.demo.repository;

import com.example.demo.models.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends MongoRepository<Message,String> {
    List<Message> findMessagesBySender(String sender);
    List<Message> findMessagesByReceiver(String receiver);
    List<Message> findMessagesBySenderAndReceiver(String sender,String receiver);
    void deleteBySender(String idSender);
    void deleteByReceiver(String idReceiver);
}
