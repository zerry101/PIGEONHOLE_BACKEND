package com.appbackend.example.AppBackend.models.PaginationModel;


import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SearchInfo {


    private Integer pageOffset;

    private Integer pageSize;

    private List<SortField> sortFields; // Use a List of SortField objects



//    private Object filters;


}
