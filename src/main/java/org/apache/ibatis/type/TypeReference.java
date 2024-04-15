/**
 *    Copyright 2009-2016 the original author or authors.
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
package org.apache.ibatis.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * References a generic type.
 *
 * @param <T> the referenced type
 * @since 3.1.0
 * @author Simone Tripodi
 */
public abstract class TypeReference<T> {

  private final Type rawType;

  protected TypeReference() {
    rawType = getSuperclassTypeParameter(getClass());
  }


  /**
   * 提供的代码是Apache iBatis库中`TypeReference`类的一个方法。这个方法叫做`getSuperclassTypeParameter`，它接收一个`Class<?>`对象作为参数。这个方法的目的是获取泛型超类的类型参数。
   *
   * 首先，方法通过在提供的`Class<?>`对象上调用`getGenericSuperclass()`来获取直接超类的`Type`。这个`Type`被存储在`genericSuperclass`变量中。
   *
   * ```java
   * Type genericSuperclass = clazz.getGenericSuperclass();
   * ```
   *
   * 接下来，方法检查`genericSuperclass`是否是`Class`的实例。如果是，那么意味着超类不是一个参数化类型（即，它不是一个泛型类），所以方法试图向上爬升类层次结构以找到一个泛型超类。
   *
   * ```java
   * if (genericSuperclass instanceof Class) {
   *   // try to climb up the hierarchy until meet something useful
   *   if (TypeReference.class != genericSuperclass) {
   *     return getSuperclassTypeParameter(clazz.getSuperclass());
   *   }
   * ```
   *
   * 如果超类是`TypeReference`但它没有参数化，那么方法会抛出一个`TypeException`。
   *
   * ```java
   * throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
   *   + "Remove the extension or add a type parameter to it.");
   * ```
   *
   * 如果`genericSuperclass`不是`Class`的实例，那么意味着它是一个参数化类型（即，它是一个泛型类）。在这种情况下，方法获取`genericSuperclass`的实际类型参数，并将第一个存储在`rawType`变量中。
   *
   * ```java
   * Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
   * ```
   *
   * 如果`rawType`也是一个`ParameterizedType`，那么方法获取它的原始类型。
   *
   * ```java
   * if (rawType instanceof ParameterizedType) {
   *   rawType = ((ParameterizedType) rawType).getRawType();
   * }
   * ```
   *
   * 最后，方法返回`rawType`，它代表了泛型超类的类型参数。
   * @return
   */
  Type getSuperclassTypeParameter(Class<?> clazz) {
    Type genericSuperclass = clazz.getGenericSuperclass();
    if (genericSuperclass instanceof Class) {
      // try to climb up the hierarchy until meet something useful
      if (TypeReference.class != genericSuperclass) {
        return getSuperclassTypeParameter(clazz.getSuperclass());
      }

      throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
        + "Remove the extension or add a type parameter to it.");
    }

    Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    // TODO remove this when Reflector is fixed to return Types
    if (rawType instanceof ParameterizedType) {
      rawType = ((ParameterizedType) rawType).getRawType();
    }

    return rawType;
  }



  public final Type getRawType() {
    return rawType;
  }

  @Override
  public String toString() {
    return rawType.toString();
  }

}
