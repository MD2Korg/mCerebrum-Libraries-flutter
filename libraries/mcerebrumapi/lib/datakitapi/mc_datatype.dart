
/*
class MCDataType{
 static const String INTEGER,
 DOUBLE,
 STRING,
 OBJECT
}
*/
class MCDataType {
 final String _value;
 const MCDataType._internal(this._value);
 toString() => '$_value';
 static MCDataType fromString(String str){
  switch(str){
   case "INTEGER": return INTEGER;
   case "DOUBLE": return DOUBLE;
   case "STRING": return STRING;
   case "OBJECT": return OBJECT;
   default: return OBJECT;
  }
 }

 static const INTEGER = const MCDataType._internal('INTEGER');
 static const DOUBLE = const MCDataType._internal('DOUBLE');
 static const STRING = const MCDataType._internal('STRING');
 static const OBJECT = const MCDataType._internal('OBJECT');

}