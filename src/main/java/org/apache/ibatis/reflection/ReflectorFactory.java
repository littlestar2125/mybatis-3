/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection;

/**
 * 反射对象工厂接口，定义反射对象的工厂
 */
public interface ReflectorFactory {

  /**
   * 是否启用类缓存
   * 默认启用
   *
   * @return 是否启用类缓存
   */
  boolean isClassCacheEnabled();

  /**
   * 设置是否启用类缓存
   *
   * @param classCacheEnabled 是否启用类缓存
   */
  void setClassCacheEnabled(boolean classCacheEnabled);

  /**
   * 查找类的反射对象
   *
   * @param type 类
   * @return 反射对象
   */
  Reflector findForClass(Class<?> type);
}
