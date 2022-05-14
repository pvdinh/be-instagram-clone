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
    public List<HistorySearchUserResult> findAll() {
        List<HistorySearchUserResult> historySearchUserResults = new ArrayList<>();
        List<HistorySearchUser> historySearchUsers = historySearchUserRepository.findHistorySearchUserByIdUser(userAccountService.getUID());
        // Sắp xếp giảm dần theo thời gian tìm kiếm
        historySearchUsers.sort(new Comparator<HistorySearchUser>() {
            @Override
            public int compare(HistorySearchUser o1, HistorySearchUser o2) {
                return Long.compare(o2.getDateSearch(), o1.getDateSearch());
            }
        });

        //convert to HistorySearchUserResult
        historySearchUsers.forEach(historySearchUser -> {
            UserAccountSetting userAccountSetting = userAccountSettingRepository.findUserAccountSettingById(historySearchUser.getIdSearch());
            historySearchUserResults.add(new HistorySearchUserResult(historySearchUser.getDateSearch(), userAccountSetting));
        });
        return historySearchUserResults;
    }

    public String insert(HistorySearchUser historySearchUser) {
        try {
            HistorySearchUser hs = historySearchUserRepository.findHistorySearchUserByIdUserAndIdSearch(historySearchUser.getIdUser(), historySearchUser.getIdSearch());
            if (hs != null) {
                hs.setDateSearch(System.currentTimeMillis());
                historySearchUserRepository.save(hs);
            } else historySearchUserRepository.insert(historySearchUser);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public String remove(HistorySearchUser historySearchUser) {
        try {
            historySearchUserRepository.delete(historySearchUser);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

    public String deleteByIdUser(String idUser) {
        try {
            historySearchUserRepository.deleteByIdUser(idUser);
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }

}
