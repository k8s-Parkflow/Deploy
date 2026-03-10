from django.conf import settings
from django.contrib import admin
from django.urls import path
from django.db import connections
from django.http import JsonResponse
from drf_spectacular.views import SpectacularAPIView, SpectacularSwaggerView

# 에러 핸들러 임포트
from park_py.error_handling import handler404 as json_handler404
from park_py.error_handling import handler500 as json_handler500

# 실제 서비스 뷰 임포트
from parking_query_service.views import availability

# 1. DB 헬스체크 함수 (쿠버네티스/인그레스 확인용)
def db_check(request):
    status = {}
    for db_name in ['command_db', 'query_db', 'vehicle_db', 'zone_db']:
        try:
            connections[db_name].ensure_connection()
            status[db_name] = "OK"
        except Exception as e:
            status[db_name] = f"Fail: {str(e)}"
    return JsonResponse(status)

# 2. URL 패턴 설정
urlpatterns = [
    # 헬스체크 (가장 먼저 확인해야 할 주소)
    path('api/health/db/', db_check),
    
    # 실제 비즈니스 로직
    path("api/zones/availabilities", availability),
    
    # API 문서 (Swagger)
    path("api/schema/", SpectacularAPIView.as_view(), name="schema"),
    path("api/docs/", SpectacularSwaggerView.as_view(url_name="schema"), name="swagger-ui"),
]

# 관리자 페이지 (설정에 있을 경우만 추가)
if "django.contrib.admin" in settings.INSTALLED_APPS:
    urlpatterns.append(path("admin/", admin.site.urls))

# 3. 에러 핸들러 설정
handler404 = json_handler404
handler500 = json_handler500
