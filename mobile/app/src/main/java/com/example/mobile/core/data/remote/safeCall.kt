package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.dto.ResponseDto
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext
//
//suspend inline fun <reified T,> safeCall(
//    execute: () -> ResponseDto<T>,
//    executeLocal: (T) -> Unit = {}
//): NetworkResult<T>{
//    val result = try{
//        execute()
//    }catch (e: UnresolvedAddressException){
//        return NetworkResult.Error(code = 520, message = "NO INTERNET")
//    }catch (e: SerializationException){
//        return NetworkResult.Error(code = 520, message = "SERIALIZATION")
//    }catch (e: Exception){
//        coroutineContext.ensureActive()
//        return NetworkResult.Error(code = 520, message = "UNKNOWN")
//    }
//    Timber.d("SAFE CALL $result")
//    return responseToResult(result,executeLocal)
//}

//inline fun <reified T> responseToResult(
//    response: ResponseDto<T>,
//    executeLocal: (T) -> Unit = {}
//): NetworkResult<T>{
//    return when(response.status!!.toInt()){
//        in 200 .. 299 ->{
//            executeLocal(response.data)
//            NetworkResult.Success(data = response.data, message = response.message)
//        }
//        401 -> NetworkResult.Error(code = response.status.toInt(), message = "No User")
//        408 -> NetworkResult.Error(code = response.status.toInt(), message = "Request Timeout")
//        429 -> NetworkResult.Error(code = response.status.toInt(), message = "Too many request")
//        in 500 .. 599 -> NetworkResult.Error(code = response.status.toInt(), message = "Server Error")
//        else -> NetworkResult.Error(code = response.status.toInt(), message = "Unknown")
//    }
//}