from django.urls import path
from .views import (
    ListUserSensorsView,
    GetLatestSensorDataView,
    GetUserByEmailView,
)

urlpatterns = [
    path('users/by-email/', GetUserByEmailView.as_view(), name='get-user-by-email'),
    path('users/<int:user_id>/sensors/', ListUserSensorsView.as_view(), name='list-user-sensors'),
    path('sensors/<str:sensor_id>/latest-data/', GetLatestSensorDataView.as_view(), name='get-latest-sensor-data'),
]
