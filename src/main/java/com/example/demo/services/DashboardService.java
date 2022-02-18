package com.example.demo.services;

import com.example.demo.models.AuthProvider;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserAccountRepository;
import com.mongodb.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Arrays;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Service
public class DashboardService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ReportRepository reportRepository;

    public HashMap<Object,Object> statisticalOverview(){
        HashMap<Object,Object> hashMap = new HashMap<>();
        try {
            hashMap.put("totalUser",userAccountRepository.findAll().size());
            hashMap.put("totalPost",postRepository.findAll().size());
            hashMap.put("totalFeedback",feedbackRepository.findAll().size());
            hashMap.put("totalReport",reportRepository.findAll().size());
            return hashMap;
        }catch (Exception e){
            return hashMap;
        }
    }

    public HashMap<Object,Object> percentageAuthUser(){
        HashMap<Object,Object> hashMap = new HashMap<>();
        try {
            hashMap.put("facebook",userAccountRepository.findByAuthProvider(AuthProvider.facebook).size());
            hashMap.put("local",userAccountRepository.findByAuthProvider(AuthProvider.local).size());
            return hashMap;
        }catch (Exception e){
            return hashMap;
        }
    }

    public HashMap<Object,Object> chartData(int year){
        HashMap<Object,Object> objects = new HashMap<>();
        try {
            MongoClient mongoClient = new MongoClient(
                    new MongoClientURI(
                            "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB+Compass&directConnection=true&ssl=false"
                    )
            );
            MongoDatabase database = mongoClient.getDatabase("instagram-clone");
            //post
            HashMap<Object,Object> hashMapP = initChart();
            MongoCollection<Document> collection = database.getCollection("post");

            AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$addFields",
                            new Document("year",
                                    new Document("$year",
                                            new Document("$toDate", "$dateCreated")))),
                    new Document("$match",
                            new Document("year", year)),
                    new Document("$group",
                            new Document("_id",
                                    new Document("$month",
                                            new Document("$toDate", "$dateCreated")))
                                    .append("count",
                                            new Document("$sum", 1L)))));
            MongoCursor<Document> mongoCursor = result.iterator();
            while (mongoCursor.hasNext()){
                Document next = mongoCursor.next();
                hashMapP.put(next.get("_id"),next.get("count"));
            }

            //userAccountSetting
            HashMap<Object,Object> hashMapUAS = initChart();
            MongoCollection<Document> collectionUAS = database.getCollection("userAccountSetting");

            AggregateIterable<Document> resultUAS = collectionUAS.aggregate(Arrays.asList(new Document("$addFields",
                            new Document("year",
                                    new Document("$year",
                                            new Document("$toDate", "$dateCreated")))),
                    new Document("$match",
                            new Document("year", year)),
                    new Document("$group",
                            new Document("_id",
                                    new Document("$month",
                                            new Document("$toDate", "$dateCreated")))
                                    .append("count",
                                            new Document("$sum", 1L)))));
            MongoCursor<Document> mongoCursorUAS = resultUAS.iterator();
            while (mongoCursorUAS.hasNext()){
                Document next = mongoCursorUAS.next();
                hashMapUAS.put(next.get("_id"),next.get("count"));
            }
            //
            objects.put("user",hashMapUAS);
            objects.put("post",hashMapP);
            return objects;
        }catch (Exception e){
            return objects;
        }
    }

    public HashMap<Object,Object> initChart(){
        HashMap<Object,Object> quantity = new HashMap<Object, Object>();
        quantity.put(1,0);
        quantity.put(2,0);
        quantity.put(3,0);
        quantity.put(4,0);
        quantity.put(5,0);
        quantity.put(6,0);
        quantity.put(7,0);
        quantity.put(8,0);
        quantity.put(9,0);
        quantity.put(10,0);
        quantity.put(11,0);
        quantity.put(12,0);
        return  quantity;
    }

}
