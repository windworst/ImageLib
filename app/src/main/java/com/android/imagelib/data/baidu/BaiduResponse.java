package com.android.imagelib.data.baidu;

import java.util.ArrayList;
import java.util.List;

public class BaiduResponse {
    public int start_index, return_number, totalNum;
    public List<BaiduDataItem> data = new ArrayList<>();
}
