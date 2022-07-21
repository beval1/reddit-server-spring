package com.beval.server.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableDTO<T> {
    private List<T> pageContent;
    private int pageNo;
    private int pageSize;
    private int pageElements;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private String sortedBy;
}
