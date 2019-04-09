// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
class DataSourceInfo {
  DataSourceInfo(this.uuid, this.dataSource, this.dataCount, this.dataCountLastHour, this.lastDataTime);
  final String uuid;
  final String dataSource;
  final int dataCount;
  final int dataCountLastHour;
  final int lastDataTime;
  bool selected = false;
}

