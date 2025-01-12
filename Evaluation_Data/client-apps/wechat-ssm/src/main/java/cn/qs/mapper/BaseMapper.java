package cn.qs.mapper;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 通用的CRUDMapper接口
 * 
 * @author Administrator
 *
 * @param <T>
 * @param <E>
 */
@NoRepositoryBean
public interface BaseMapper<T, E extends Serializable> extends JpaRepository<T, E> {

}
