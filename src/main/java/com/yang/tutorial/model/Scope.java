package com.yang.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author yangz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Scope {

    private String id;

    private List<String> subId;

}
