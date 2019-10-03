
class MCSummaryLevel {
 final String _value;
 const MCSummaryLevel._internal(this._value);
 toString() => '$_value';
 static MCSummaryLevel fromString(String str){
  switch(str){
   case "MINUTE": return MINUTE;
   case "HOUR": return HOUR;
   case "DAY": return DAY;
   default: return MINUTE;
  }
 }

 static const MINUTE = const MCSummaryLevel._internal('MINUTE');
 static const HOUR = const MCSummaryLevel._internal('HOUR');
 static const DAY = const MCSummaryLevel._internal('DAY');

}