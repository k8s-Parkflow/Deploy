class MultiServiceRouter:
    # 앱 이름과 DB 별칭 매핑
    route_app_labels = {
        'parking_command_service': 'command_db',
        'parking_query_service': 'query_db',
        'vehicle_service': 'vehicle_db',
        'zone_service': 'zone_db',
    }

    def db_for_read(self, model, **hints):
        return self.route_app_labels.get(model._meta.app_label)

    def db_for_write(self, model, **hints):
        return self.route_app_labels.get(model._meta.app_label)

    def allow_relation(self, obj1, obj2, **hints):
        # 동일한 DB 내의 관계만 허용
        db1 = self.route_app_labels.get(obj1._meta.app_label)
        db2 = self.route_app_labels.get(obj2._meta.app_label)
        if db1 and db2:
            return db1 == db2
        return None

    def allow_migrate(self, db, app_label, model_name=None, **hints):
        # 지정된 DB에만 마이그레이션 실행
        if app_label in self.route_app_labels:
            return db == self.route_app_labels[app_label]
        return None