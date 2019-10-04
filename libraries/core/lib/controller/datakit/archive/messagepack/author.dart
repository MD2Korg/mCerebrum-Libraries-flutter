class Author {
  String name;
  String email;

  Author(String name, String email) {
    this.name = name;
    this.email = email;
  }

  Map toJson() {
    Map m = new Map();
    m["name"] = name;
    m["email"] = email;
    return m;
  }

  factory Author.fromJson(Map<String, dynamic> json) {
    Author d = Author(json["name"], json["email"]);
    return d;
  }
}
