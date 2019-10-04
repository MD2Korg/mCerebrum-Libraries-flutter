import 'dart:convert';

import 'package:core/controller/datakit/archive/messagepack/stream_metadata.dart';
import 'package:core/controller/error_code/ErrorCode.dart';
import 'package:dio/dio.dart';
import 'package:http_parser/http_parser.dart';

import 'login_response.dart';
import 'register_response.dart';

class CerebralCortex {
  Dio _dio;
  String url;

  void init(String url) {
    _dio = Dio();
    this.url = url;
    _dio.options.baseUrl = url;
/*
    _dio.options.connectTimeout = 20000;
    _dio.options.receiveTimeout = 20000;
*/
  }

  Future<LoginResponse> login(String userId, String password) async {
    Map<String, dynamic> m = new Map();
    m["username"] = userId;
    m["password"] = password;
    try {
      Response response = await _dio.post("/api/v3/user/login",
          options: Options(headers: {"Content-Type": "application/json"}),
          data: json.encode(m));
      return LoginResponse(
          response.data["auth_token"], response.data["user_uuid"]);
    } on DioError catch (error) {
      if (error.type == DioErrorType.DEFAULT)
        throw Exception(ErrorCode.SERVER_NOT_FOUND.toString());
      else if (error.type == DioErrorType.RESPONSE)
        throw Exception(ErrorCode.INVALID_LOGIN);
      else
        throw Exception(ErrorCode.CONNECTION_TIMEOUT);
    } catch (error) {
      throw Exception(error.toString());
    }
  }

  Future<bool> uploadData(LoginResponse loginResponse,
      RegisterResponse registerResponse, String filePath) async {
    try {
      var formData = FormData.fromMap({
        "file": MultipartFile.fromFileSync(filePath,
            filename: "a.gzip", contentType: MediaType('application', 'gzip'))
      });

      var response = await _dio.put("/api/v3/stream/" + registerResponse.hashId,
          options: Options(headers: {
            "Authorization": loginResponse.authToken,
          }),
          data: formData);
      print(response.statusCode.toString() + " " + response.statusMessage);

      if (response.statusCode == 200) {
        return true;
      } else
        throw Exception(response.statusMessage);
    } on DioError catch (error) {
      print(error.toString());
      if (error.type == DioErrorType.DEFAULT)
        throw Exception(ErrorCode.SERVER_NOT_FOUND.toString());
      else if (error.type == DioErrorType.RESPONSE)
        throw Exception(ErrorCode.UPLOAD_FAILED);
      else
        throw Exception(ErrorCode.CONNECTION_TIMEOUT);
    } catch (error) {
      print("error = " + error.toString());
      throw Exception(error.toString());
    }
  }

  Future<RegisterResponse> registerDataSource(
      LoginResponse loginResponse, StreamMetadata streamMetaData) async {
    try {
      Response response = await _dio.post("/api/v3/stream/register",
          options: Options(headers: {
            "Authorization": loginResponse.authToken,
            "Content-Type": "application/json"
          }),
          data: json.encode(streamMetaData));

      if (response.statusCode == 200) {
        print("register datasource success");
        return RegisterResponse(
            response.data["message"], response.data["hash_id"]);
      } else
        print("register datasource failed");
      throw Exception(response.statusMessage);
    } on DioError catch (error) {
      if (error.type == DioErrorType.DEFAULT)
        throw Exception(ErrorCode.SERVER_NOT_FOUND.toString());
      else if (error.type == DioErrorType.RESPONSE)
        throw Exception(ErrorCode.INVALID_LOGIN);
      else
        throw Exception(ErrorCode.CONNECTION_TIMEOUT);
    } catch (error) {
      throw Exception(error.toString());
    }
  }
}
