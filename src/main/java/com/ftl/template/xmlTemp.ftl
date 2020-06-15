<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packetageDAO}.${entityName}Dao" >

    <resultMap id="${entityName}ResultMap" type="${entityType}">
        <#list columnList as column>
            <#if column_index = 0>
                <id column="id" property="id"/>
            <#else>
                <result column="${column.nativeColumn}" property="${column.convertColumn}"/>
            </#if>
        </#list>
    </resultMap>

    <insert id="save"  parameterType="${entityType}">
        insert into ${tableName} (
        <#list columnList as column>
            <#if column_index = 0>
                ${column.nativeColumn}
            <#else>
                ,${column.nativeColumn}
            </#if>
        </#list>
        )
        values (
        <#list columnList as column>
            <#if column_index = 0>
                ${column.convertColumn}
            <#else>
                ,${column.convertColumn}
            </#if>
        </#list>
        )
    </insert>

</mapper>