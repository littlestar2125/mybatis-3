/**
 * Copyright 2009-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * 怎样从java对象转换为数据库对象及怎么样从数据库获取对象
 * <p>
 * 泛型
 *
 * @author Clinton Begin
 */
public interface TypeHandler<T> {

  /**
   * 设置参数
   *
   * @param ps        PreparedStatement
   * @param i         参数索引
   * @param parameter 参数
   * @param jdbcType  jdbc类型
   * @throws SQLException SQL异常
   */
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  /**
   * Gets the result.
   *
   * @param rs         the rs
   * @param columnName Colunm name, when configuration <code>useColumnLabel</code> is <code>false</code>
   * @return the result
   * @throws SQLException the SQL exception
   */
  T getResult(ResultSet rs, String columnName) throws SQLException;

  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  /**
   * 从存储过程中获取结果
   * @param cs CallableStatement
   * @param columnIndex 索引
   * @return 结果
   * @throws SQLException SQL异常
   */
  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
