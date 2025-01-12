package com.car.txlcn;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * TX-LCN分布式事务协调器
 * @author xlj
 */
@SpringBootApplication
@EnableTransactionManagerServer
public class TxManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxManageApplication.class, args);
    }


}

