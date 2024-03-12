
package com.appbackend.example.AppBackend.services.AdminServices;

import com.appbackend.example.AppBackend.entities.User;
import com.appbackend.example.AppBackend.models.ErrorDto;
import com.appbackend.example.AppBackend.models.UserListData;
import com.appbackend.example.AppBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class UserDataService {

    @Autowired
    UserRepository userRepository;



    public UserListData getUsers(Integer pageNo,Integer pageSize,String sortBy){

        int pageSizeLimit=1000;

        if(pageSize>pageSizeLimit){
            ErrorDto errorDto= ErrorDto.builder().code(400).status("ERROR").message("maximum page size exceeded (Max: 500)").build();

            return new UserListData(null,errorDto);

        }

            PageRequest pageRequest= PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
            Page<User> userPage=userRepository.findAll(pageRequest);
//        List<User> userList=userPage.getContent();

        return new UserListData(userPage,null);






    }

}
