package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class UserPicPatchReq {
    private MultipartFile pic;

    @JsonIgnore
    private long signedUserId;

    @JsonIgnore
    private String picName;
}
