package com.creditstate.demo.entity.po;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author weiming.zhu
 * @date 2020/4/26 14:08
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Field("id")
    private Integer id;
    @Field("username")
    private String username;
    @Field("password")
    private String password;
}
