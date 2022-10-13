package com.topband.shop.config;

import javax.validation.groups.Default;

/**
 * 自定义 Validate 校验分组
 *
 * @author huangyijun
 */
public interface CustomValidatedGroup extends Default {
    interface Crud extends CustomValidatedGroup {
        interface Create extends Crud {

        }

        interface Update extends Crud {

        }

        interface Query extends Crud {

        }

        interface Delete extends Crud {

        }
    }
}
