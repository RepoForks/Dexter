/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.dexter;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.PermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.Collection;

public class PermissionsListenerToPermissionListenerAdapter implements PermissionsListener {

  private final PermissionListener listener;

  public PermissionsListenerToPermissionListenerAdapter(PermissionListener listener) {
    this.listener = listener;
  }

  @Override public void onPermissionsChecked(PermissionsReport report) {
    Collection<PermissionDeniedResponse> deniedResponses = report.getDeniedPermissionResponses();
    Collection<PermissionGrantedResponse> grantedResponses = report.getGrantedPermissionResponses();
    if (!deniedResponses.isEmpty()) {
      PermissionDeniedResponse response = getFirstFromCollection(deniedResponses);
      listener.onPermissionDenied(response);
    } else {
      PermissionGrantedResponse response = getFirstFromCollection(grantedResponses);
      listener.onPermissionGranted(response);
    }
  }

  @Override public void onPermissionRationaleShouldBeShown(Collection<PermissionRequest> requests,
      PermissionToken token) {
    PermissionRequest firstRequest = getFirstFromCollection(requests);
    listener.onPermissionRationaleShouldBeShown(firstRequest, token);
  }

  private <T> T getFirstFromCollection(Collection<T> items) {
    T firstItem = null;
    for (T item : items) {
      firstItem = item;
    }
    return firstItem;
  }
}
