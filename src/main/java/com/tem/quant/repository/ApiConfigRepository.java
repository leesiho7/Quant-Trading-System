

package com.tem.quant.repository;

import com.tem.quant.entity.ApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiConfigRepository extends JpaRepository<ApiConfig, Long> {
    // 기본 CRUD 기능 제공
}