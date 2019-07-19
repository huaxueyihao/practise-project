package com.gardenia.gmall.user.bean;


import lombok.*;

import java.io.Serializable;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UmsMember implements Serializable {

    private static final long serialVersionUID = 238978900313944172L;

    private Integer id;

    private String username;



}
