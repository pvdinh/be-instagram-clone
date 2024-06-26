package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.activity.Activity;
import com.example.demo.models.blockPost.BlockPost;
import com.example.demo.models.comment.Comment;
import com.example.demo.models.group.Group;
import com.example.demo.models.group.GroupMember;
import com.example.demo.models.profile.PostDetail;
import com.example.demo.repository.*;
import com.example.demo.utils.SortClassCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.client.*;

import java.util.Arrays;

import org.bson.Document;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlockPostRepository blockPostRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private LikeCommentRepository likeCommentRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private ReplyCommentRepository replyCommentRepository;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findAllByUserId(String userId) {
        return postRepository.findPostByUserId(userId);
    }

    public Post findByPostId(String pId) {
        return postRepository.findPostById(pId);
    }

    public List<Post> findContainsByUserIdOrIdPageable(String search, int page, int size) {
        List<Post> posts = findAll();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            posts = postRepository.findByUserIdContainsOrId(search, search, pageable);
            return posts;
        } catch (Exception e) {
            return posts;
        }
    }

    public List<Post> findContainsByUserIdOrId(String search) {
        List<Post> posts = new ArrayList<>();
        try {
            posts = postRepository.findByUserIdContainsOrId(search, search);
            return posts;
        } catch (Exception e) {
            return posts;
        }
    }

    public List<Post> findPostFilter(int typeFilter, int page, int size) {
        List<Post> posts = new ArrayList<>();
        try {
            switch (typeFilter) {
                case 1:
                    Pageable pageableASC = PageRequest.of(page, size, Sort.by("dateCreated").ascending());
                    posts = postRepository.findAll(pageableASC).getContent();
                    break;
                case 2:
                    Pageable pageableDES = PageRequest.of(page, size, Sort.by("dateCreated").descending());
                    posts = postRepository.findAll(pageableDES).getContent();
                    break;
                case 3: {
                    List<Post> finalPosts = posts;
                    getPostDetailPopular(page, size).forEach(postDetail -> {
                        finalPosts.add(postDetail.getPost());
                    });
                    return finalPosts;
                }
                default:
                    break;
            }
            return posts;
        } catch (Exception e) {
            return posts;
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public Post getTop1Like() {
        Post post = new Post();
        MongoCollection<Document> collection = mongoTemplate.getCollection("post");


        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$addFields",
                        new Document("idPost",
                                new Document("$toString", "$_id"))),
                new Document("$lookup",
                        new Document("from", "like")
                                .append("localField", "idPost")
                                .append("foreignField", "idPost")
                                .append("as", "likes")),
                new Document("$addFields",
                        new Document("countLike",
                                new Document("$size", "$likes"))),
                new Document("$sort",
                        new Document("countLike", -1L)),
                new Document("$match",
                        new Document("isBlock", 0L)
                                .append("privacy", 0L)),
                new Document("$limit", 1L)));
        MongoCursor<Document> mongoCursor = result.iterator();
        while (mongoCursor.hasNext()) {
            Document next = mongoCursor.next();
            String id = next.get("_id").toString();
            String caption = next.get("caption").toString();
            String imagePath = next.get("imagePath").toString();
            String tag = next.get("tags").toString();
            String userId = next.get("userId").toString();
            String type = next.get("type").toString();
            Object o = next.get("videoPath");
            String videoPath = "";
            if (o != null) videoPath = o.toString();
            long dateCreated = Long.parseLong(next.get("dateCreated").toString());
            post = new Post(id, caption, imagePath, tag, userId, dateCreated, Collections.emptyList(), type, videoPath);
        }
        return post;
    }

    public Post getTop1Comment() {
        Post post = null;
        MongoCollection<Document> collection = mongoTemplate.getCollection("post");


        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$addFields",
                        new Document("idPost",
                                new Document("$toString", "$_id"))),
                new Document("$lookup",
                        new Document("from", "comment")
                                .append("localField", "idPost")
                                .append("foreignField", "idPost")
                                .append("as", "comments")),
                new Document("$lookup",
                        new Document("from", "replyComment")
                                .append("localField", "idPost")
                                .append("foreignField", "idPost")
                                .append("as", "replyComments")),
                new Document("$addFields",
                        new Document("countComment",
                                new Document("$size", "$comments"))
                                .append("countReplyComment",
                                        new Document("$size", "$replyComments"))),
                new Document("$addFields",
                        new Document("count",
                                new Document("$add", Arrays.asList("$countComment", "$countReplyComment")))),
                new Document("$sort",
                        new Document("count", -1L)),
                new Document("$match",
                        new Document("isBlock", 0L)
                                .append("privacy", 0L)),
                new Document("$limit", 1L)));
        MongoCursor<Document> mongoCursor = result.iterator();
        while (mongoCursor.hasNext()) {
            Document next = mongoCursor.next();
            String id = next.get("_id").toString();
            String caption = next.get("caption").toString();
            String imagePath = next.get("imagePath").toString();
            String tag = next.get("tags").toString();
            String userId = next.get("userId").toString();
            String type = next.get("type").toString();
            Object o = next.get("videoPath");
            String videoPath = "";
            if (o != null) videoPath = o.toString();
            long dateCreated = Long.parseLong(next.get("dateCreated").toString());
            post = new Post(id, caption, imagePath, tag, userId, dateCreated, Collections.emptyList(), type, videoPath);
        }
        return post;
    }

    public Post getTop1Save() {
        Post post = null;
        MongoCollection<Document> collection = mongoTemplate.getCollection("post");


        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$addFields",
                        new Document("idPost",
                                new Document("$toString", "$_id"))),
                new Document("$lookup",
                        new Document("from", "savedPost")
                                .append("localField", "idPost")
                                .append("foreignField", "postId")
                                .append("as", "saves")),
                new Document("$addFields",
                        new Document("countSave",
                                new Document("$size", "$saves"))),
                new Document("$sort",
                        new Document("countSave", -1L)),
                new Document("$match",
                        new Document("isBlock", 0L)
                                .append("privacy", 0L)),
                new Document("$limit", 1L)));
        MongoCursor<Document> mongoCursor = result.iterator();
        while (mongoCursor.hasNext()) {
            Document next = mongoCursor.next();
            String id = next.get("_id").toString();
            String caption = next.get("caption").toString();
            String imagePath = next.get("imagePath").toString();
            String tag = next.get("tags").toString();
            String userId = next.get("userId").toString();
            String type = next.get("type").toString();
            Object o = next.get("videoPath");
            String videoPath = "";
            if (o != null) videoPath = o.toString();
            long dateCreated = Long.parseLong(next.get("dateCreated").toString());
            post = new Post(id, caption, imagePath, tag, userId, dateCreated, Collections.emptyList(), type, videoPath);
        }
        return post;
    }

    public Post getTop1Popular() {
        Post post = null;
        List<PostDetail> postDetails = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        posts.forEach(p -> {
            List<Comment> comments = commentRepository.findCommentByIdPost(p.getId());
            postDetails.add(new PostDetail(p, comments.size(), Collections.emptyList()));
        });
        postDetails.sort(new SortClassCustom.PostByPopular());
        for (int i = 0; i < postDetails.size(); i++) {
            if (postDetails.get(i).getPost().getIsBlock() != 1) {
                post = postDetails.get(i).getPost();
                break;
            }
        }
        return post;
    }

    public List<PostDetail> getPostDetailPopular(int page, int size) {
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            List<Post> posts = postRepository.findAll();
            posts.forEach(post -> {
                List<Comment> comments = commentRepository.findCommentByIdPost(post.getId());
                postDetails.add(new PostDetail(post, comments.size(), Collections.emptyList()));
            });
            postDetails.sort(new SortClassCustom.PostByPopular());
            if((page * size) > postDetails.size()){
                return Collections.EMPTY_LIST;
            }else {
                PagedListHolder pageable = new PagedListHolder(postDetails);
                pageable.setPageSize(size);
                pageable.setPage(page);
                return pageable.getPageList();
            }
        } catch (Exception e) {
            return postDetails;
        }
    }

    public List<Post> findAllByListUserId(List<String> listUserId) {
        List<Post> posts = new ArrayList<>();
        listUserId.forEach(s -> {
            posts.addAll(postRepository.findPostByUserId(s));
        });
        return posts;
    }

    public PostInformation getPostInformationOfUser(String pId) {
        Post post = postRepository.findPostById(pId);
        if (post != null) {
            if (post.getUserId().equals(userAccountService.getUID())) {
                UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(post.getUserId());
                Group group = groupService.findById(post.getIdGroup());
                return new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()), group);
            }
            if (post.getPrivacy() == 0) {
                UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(post.getUserId());
                Group group = groupService.findById(post.getIdGroup());
                return new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()), group);
            }
            return new PostInformation(null, null, null, null);
        } else {
            return new PostInformation(null, null, null, null);
        }
    }

    public PostInformation adminGetPostInformation(String pId) {
        Post post = postRepository.findPostById(pId);
        if (post != null) {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(post.getUserId());
            Group group = groupService.findById(post.getIdGroup());
            return new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()), group);
        } else {
            return new PostInformation(null, null, null, null);
        }
    }


    public List<PostInformation> getAllPostInformationFollowing(int page, int size) {
        List<PostInformation> postInformations = new ArrayList<>();
        try {
            List<UserAccountSetting> userAccountSettings = new ArrayList<>();
            List<Follow> follows = followService.findFollowByUserCurrent();
            follows.forEach(follow -> {
                userAccountSettings.add(userAccountSettingRepository.findUserAccountSettingById(follow.getUserFollowing()));
            });
            userAccountSettings.forEach(userAccountSetting -> {
                List<Post> posts = postRepository.findPostByUserId(userAccountSetting.getId());
                posts.forEach(post -> {
                    //neu privacy la == 0 (public) thi se duoc hien thi voi nhung nguoi theo doi
                    //2 nguoi trong cung nhom moi nhin thay bai dang cau nhau o trang home
                    if (post.getPrivacy() == 0) {
                        Group group = groupService.findById(post.getIdGroup());
                        if (group != null && checkExistsInGroup(post.getUserId(), userAccountService.getUID(), group.getId())) {
                            postInformations.add(new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()), group));
                        } else if (group != null && !checkExistsInGroup(post.getUserId(), userAccountService.getUID(), group.getId())) {

                        } else
                            postInformations.add(new PostInformation(post, userAccountSetting, likeService.getListUserLikedPost(post.getId()), group));

                    }
                });
            });
            postInformations.sort(new SortClassCustom.PostByDateCreate());
            if((page * size) > postInformations.size()){
                return Collections.EMPTY_LIST;
            }else {
                PagedListHolder pageable = new PagedListHolder(postInformations);
                pageable.setPageSize(size);
                pageable.setPage(page);
                return pageable.getPageList();
            }
        } catch (Exception e) {
            return postInformations;
        }
    }

    public boolean checkExistsInGroup(String u1, String u2, String g) {
        GroupMember g1 = groupMemberRepository.findByIdGroupAndIdUser(g, u1);
        GroupMember g2 = groupMemberRepository.findByIdGroupAndIdUser(g, u2);
        if (g1 != null && g2 != null && g1.getStatus() == 1 && g2.getStatus() == 1) {
            return true;
        } else return false;
    }

    public String like(String uId, String pId) {
        Like like = new Like(pId, uId, System.currentTimeMillis());
        Like likeD = likeRepository.findLikeByIdUserAndIdPost(uId, pId);
        if (likeD == null) {
            likeRepository.insert(like);
            Post post = postRepository.findPostById(pId);
            post.getLikes().add(likeRepository.findLikeByIdUserAndIdPost(uId, pId).getId());
            postRepository.save(post);
            //Thêm vào activity
            if (!uId.equals(post.getUserId())) {
                activityService.insert(new Activity(userAccountService.getUID(), post.getUserId(), post.getId(), "like", 0, System.currentTimeMillis()), userAccountService.getUID());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String unLike(String uId, String pId) {
        Like like = likeRepository.findLikeByIdUserAndIdPost(uId, pId);
        if (like != null) {
            Post post = postRepository.findPostById(pId);
            post.getLikes().remove(likeRepository.findLikeByIdUserAndIdPost(uId, pId).getId());
            postRepository.save(post);
            likeRepository.delete(like);
            //xoá khỏi activity
            if (!uId.equals(post.getUserId())) {
                Activity activity = activityService.findActivityByIdCurrentUserAndIdInteractUserAndTypeActivityAndIdPost(userAccountService.getUID(), post.getUserId(), "like", post.getId());
                activityService.delete(activity, userAccountService.getUID());
            }
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public String postNewPost(Post post) {
        try {
            post.setDateCreated(System.currentTimeMillis());
            post.setLikes(Collections.emptyList());
            post.setUserId(userAccountService.getUID());
            post.setIsBlock(0);
            if (post.getIdGroup() == null) post.setIdGroup("");
            if (post.getVideoPath() == null) post.setVideoPath("");
            if (post.getUserId() == "" || post.getUserId() == null) {
                return FAIL;
            } else {
                postRepository.insert(post);
                updatePostQuantity(post.getUserId());
                return SUCCESS;
            }
        } catch (Exception e) {
            System.out.println("POST EXISTS");
            return FAIL;
        }
    }

    public String changePrivacy(Post post) {
        try {
            Post p = postRepository.findPostById(post.getId());
            if (p != null && post.getUserId().equals(userAccountService.getUID())) {
                postRepository.save(post);
                return SUCCESS;
            } else return FAIL;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public String deletePost(String pId) {
        try {
            Post post = postRepository.findPostById(pId);
            GroupMember groupMember = new GroupMember();
            boolean isAdmin = false;
            if (post != null) {
                //admin group xoa bai
                groupMember = groupMemberRepository.findByIdGroupAndIdUser(post.getIdGroup(), userAccountService.getUID());
                isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            if ((post != null && (post.getUserId().equals(userAccountService.getUID()) || (groupMember != null && groupMember.getRole().equals("ADMIN")) || isAdmin))) {
                //xoa trong story
                storyRepository.deleteByIdPost(pId);
                //xoa trong activity
                activityRepository.deleteByIdPost(pId);
                //xoa trong report
                reportRepository.deleteByIdPost(pId);
                //xoa trong like
                likeRepository.deleteByIdPost(pId);
                //xoa trong save post
                savedPostRepository.deleteByPostId(pId);
                //xoa trong likeComment
                commentRepository.findCommentByIdPost(pId).forEach(comment -> {
                    likeCommentRepository.deleteByIdComment(comment.getId());
                });

                //xoa trong comment
                commentRepository.deleteByIdPost(pId);
                //xoa trongh replyComment
                replyCommentRepository.deleteAllByIdPost(pId);

                //xoa bai dang
                postRepository.delete(post);
                updatePostQuantity(post.getUserId());
                return SUCCESS;
            } else return FAIL;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public void updatePostQuantity(String idUser) {
        //Cập nhật lại số lượng bài đăng khi thêm hoặc xoá
        UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(idUser);
        userAccountSetting.setPosts(postRepository.findPostByUserId(idUser).size());
        userAccountSettingRepository.save(userAccountSetting);
    }

    public List<PostDetail> getPostVideo(String username, int page, int size) {
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            UserAccount userAccount = userAccountService.findUserAccountByUsername(username);
            if (userAccount != null) {
                List<Post> posts = postRepository.findPostVideoByTypeAndUserId("video", userAccount.getId(), pageable);
                posts.forEach(post -> {
                    List<String> stringListLike = likeService.getListUserLikedPost(post.getId());
                    int numberOfComments = commentService.findCommentByIdPost(post.getId()).size();
                    postDetails.add(new PostDetail(post, numberOfComments, stringListLike));
                });
                return postDetails;
            } else return postDetails;
        } catch (Exception e) {
            return postDetails;
        }
    }

    public List<PostDetail> getPostPrivate(String username, int page, int size) {
        List<PostDetail> postDetails = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
            UserAccount userAccount = userAccountService.findUserAccountByUsername(username);
            if (userAccount != null) {
                List<Post> posts = postRepository.findPostByPrivacyAndUserId(1, userAccount.getId(), pageable);
                posts.forEach(post -> {
                    List<String> stringListLike = likeService.getListUserLikedPost(post.getId());
                    int numberOfComments = commentService.findCommentByIdPost(post.getId()).size();
                    postDetails.add(new PostDetail(post, numberOfComments, stringListLike));
                });
                return postDetails;
            } else return postDetails;
        } catch (Exception e) {
            return postDetails;
        }
    }

    //thuực hiện khoá 1 bài đăng (sau 30 ngày tự động xoá)
    public String blockPost(String pId) {
        try {
            Post post = postRepository.findPostById(pId);
            //chuyển link ảnh(video) sang bảng BlockPost để có thể khôi phục lại
            BlockPost blockPost = new BlockPost(pId, post.getImagePath(), post.getVideoPath(), System.currentTimeMillis());
            blockPostRepository.insert(blockPost);
            //xoá link video, thay đổi link ảnh
            post.setImagePath("https://res.cloudinary.com/dinhpv/image/upload/v1644923908/instargram-clone/eyes-dont-see_z5xppf.jpg");
            post.setVideoPath("");
            post.setIsBlock(1);
            postRepository.save(post);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public String unBlockPost(String pId) {
        try {
            BlockPost blockPost = blockPostRepository.findByPostId(pId);
            Post post = postRepository.findPostById(pId);
            //cập nhật lại link video,link ảnh đã block
            post.setImagePath(blockPost.getImagePath());
            post.setVideoPath(blockPost.getVideoPath());
            post.setIsBlock(0);
            postRepository.save(post);
            //
            blockPostRepository.delete(blockPost);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public BlockPost getPostBlock(String pId) {
        BlockPost blockPost = new BlockPost();
        try {
            return blockPostRepository.findByPostId(pId);
        } catch (Exception e) {
            return blockPost;
        }
    }

    public Post findPostByLikes(String id) {
        return postRepository.findPostByLikes(id);
    }
}
