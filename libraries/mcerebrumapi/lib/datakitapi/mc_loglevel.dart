
class MCLogLevel {
 final String _value;
 const MCLogLevel._internal(this._value);
 toString() => '$_value';
 static MCLogLevel fromString(String str){
  switch(str){
   case "DEBUG": return DEBUG;
   case "WARN": return WARN;
   case "ERROR": return ERROR;
   case "EXCEPTION": return EXCEPTION;
   default: return DEBUG;
  }
 }

 static const DEBUG = const MCLogLevel._internal('DEBUG');
 static const WARN = const MCLogLevel._internal('WARN');
 static const ERROR = const MCLogLevel._internal('ERROR');
 static const EXCEPTION = const MCLogLevel._internal('EXCEPTION');

}