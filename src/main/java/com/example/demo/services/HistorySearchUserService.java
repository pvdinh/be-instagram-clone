package com.example.demo.services;

import com.example.demo.models.SearchInLayout.HistorySearchUser;
import com.example.demo.models.SearchInLayout.HistorySearchUserResult;
import com.example.demo.models.UserAccountSetting;
import com.example.demo.repository.HistorySearchUserRepository;
import com.example.demo.repository.UserAccountSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HistorySearchUserService {
    @Autowired
    private HistorySearchUserRepository historySearchUserRepository;
    @Autowired
    private UserAccountSettingRepository userAccountSettingRepository;
    @Autowired
    private UserAccountService userAccountService;

    private final String SUCCESS = "success";
    private final String FAIL = "fail";

    // kết quả trả về là 1 list User result
    public List<HistorySearchUserResult> findAll(){
        List<HistorySearchUserResult> historySearchUserResults = new ArrayList<>();
        List<HistorySearchUser> historySearchUsers = historySearchUserRepository.findHistorySearchUserByIdUser(userAccountService.getUID());
        // Sắp xếp giảm dần theo thời gian tìm kiếm
        historySearchUsers.sort(new Comparator<HistorySearchUser>() {
            @Override
            public int compare(HistorySearchUser o1, HistorySearchUser o2) {
                return Long.compare(o2.getDateSearch(),o1.getDateSearch());
            }
        });

        // lấy duy nhất 1 kết quả đối với mỗi 1 user ( mới nhất)
        historySearchUsers.forEach(historySearchUser -> {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(historySearchUser.getIdSearch());
            if(!checkExists(historySearchUserResults,userAccountSetting)){
                historySearchUserResults.add(new HistorySearchUserResult(historySearchUser.getDateSearch(),userAccountSetting));
            }
        });
        return historySearchUserResults;
    }

    public boolean checkExists(List<HistorySearchUserResult> historySearchUserResults,UserAccountSetting userAccountSetting){
        for(int i=0;i<historySearchUserResults.size();i++){
            if(userAccountSetting.getId().equalsIgnoreCase(historySearchUserResults.get(i).getUserAccountSetting().getId())){
                return true;
            }
        }
        return false;
    }

    public String insert(HistorySearchUser historySearchUser){
        try {
            historySearchUserRepository.insert(historySearchUser);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

    public String remove(HistorySearchUser historySearchUser){
        try {
            historySearchUserRepository.delete(historySearchUser);
            return SUCCESS;
        }catch (Exception e){
            return FAIL;
        }
    }

}
