
/*
class MCDataType{
 static const String INTEGER,
 DOUBLE,
 STRING,
 OBJECT
}
*/
class MCDataFormat {
 final String _value;
 const MCDataFormat._internal(this._value);
 toString() => '$_value';
 static MCDataFormat fromString(String str){
  switch(str){
   case "DATA": return DATA;
   case "SUMMARY": return SUMMARY;
   case "ANNOTATION": return ANNOTATION;
   default: return DATA;
  }
 }

 static const DATA = const MCDataFormat._internal('DATA');
 static const SUMMARY = const MCDataFormat._internal('SUMMARY');
 static const ANNOTATION = const MCDataFormat._internal('ANNOTATION');
}