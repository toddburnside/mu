/*
 * Copyright 2017-2018 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mu.rpc
package interceptors

import io.grpc.MethodDescriptor
import io.grpc.MethodDescriptor.MethodType

/**
 * This model encapsulates a friendly representation of an RPC [[io.grpc.MethodDescriptor]].
 *
 * @param serviceName Service name owning the method.
 * @param fullMethodName Service full name, in format `full.serviceName/MethodName`.
 * @param methodName Just the method name.
 * @param `type` Method Type.
 */
case class GrpcMethodInfo(
    serviceName: String,
    fullMethodName: String,
    methodName: String,
    `type`: MethodType) {

  def isClientStreaming: Boolean =
    (`type` eq MethodType.CLIENT_STREAMING) || (`type` eq MethodType.BIDI_STREAMING)

  def isServerStreaming: Boolean =
    (`type` eq MethodType.SERVER_STREAMING) || (`type` eq MethodType.BIDI_STREAMING)
}

object GrpcMethodInfo {

  def apply[Req, Res](methodDescriptor: MethodDescriptor[Req, Res]): GrpcMethodInfo = {

    val serviceName = MethodDescriptor.extractFullServiceName(methodDescriptor.getFullMethodName)
    // Full method names are of the form: "full.serviceName/MethodName". We extract the last part.
    val methodName = methodDescriptor.getFullMethodName.substring(serviceName.length + 1)

    GrpcMethodInfo(
      serviceName,
      methodDescriptor.getFullMethodName,
      methodName,
      methodDescriptor.getType)

  }

}