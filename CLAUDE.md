# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ZS-Admin is an enterprise-grade admin platform built with Spring Boot 3.x, Spring Security, MyBatis-Plus, and JWT. It supports multi-tenancy (SaaS), RBAC permissions, and multi-user-type authentication (platform admin, member, companion). Java 17 is required.

## Build & Run

```bash
# Build entire project (from root)
mvn clean package -DskipTests

# Build only zs-admin (the deployable module)
mvn clean package -DskipTests -pl zs-admin -am

# Run the application
java -jar zs-admin/target/zs-admin.jar --spring.profiles.active=dev

# Run tests
mvn test

# Run a single module's tests
mvn test -pl zs-modules/zs-system/zs-system-service
```

Application starts on port 8080 with context path `/api`. Dev profile uses `application-dev.yml`.

## Module Structure

```
zs-admin/              → Deployable Spring Boot application (main class: com.zs.ZsAdminApplication)
zs-common/             → Shared libraries
  zs-common-core/      → Base entities, Result wrapper, enums, utils, JWT, tenant context, constants
  zs-common-aop/       → @Log / @LoginLog / @Desensitize annotations + AOP aspects
  zs-common-mybatis/   → MyBatis-Plus config, multi-tenant interceptor, data permission interceptor
  zs-common-redis/     → RedisUtil wrapper
  zs-common-security/  → Spring Security config, JWT filter, auth providers, white URL properties
zs-modules/            → Business modules (each split into api + service)
  zs-system/           → System management (users, roles, menus, depts, posts, dicts, config, notices, messages, tenants)
    zs-system-api/     → Feign-style API interfaces + DTOs (cross-module consumption)
    zs-system-service/ → Controllers, services, mappers, entities, XML mapper files
  zs-file/             → File management (OSS: Aliyun, Tencent, Huawei, Local)
  zs-mail/             → Email service
  zs-sms/              → SMS service (Aliyun, Tencent)
  zs-websocket/        → WebSocket messaging
zs-quartz/             → Scheduled task management (Quartz JDBC store)
zs-generator/          → Code generator (table → CRUD boilerplate)
db/                    → SQL initialization scripts (zs_admin.sql)
```

## Architecture Patterns

### Layer Convention per Business Module
Each feature under `zs-modules/*/zs-system-service/` follows:
- `controller/` → REST controllers with `@PreAuthorize` for permission checks
- `service/` → Interface (extends `IService<Entity>`)
- `service/impl/` → Implementation
- `mapper/` → MyBatis-Plus mapper interfaces
- `domain/entity/` → Database entities (extend `BaseEntity`)
- `domain/params/` → Request params (Add/Update/Query/PageQuery)
- `domain/vo/` → Response view objects
- `domain/excel/` → Excel export models
- `resources/mapper/**/*.xml` → MyBatis XML mapper files

### Unified Response
All endpoints return `Result<T>` with `ok()` / `error()` factory methods. Pagination returns `PageResult<T>`.

### Multi-Tenancy
- Tenant ID flows: HTTP header `X-Tenant-Id` → JWT claims → `TenantContext` (ThreadLocal)
- MyBatis-Plus `TenantLineInnerInterceptor` auto-appends `tenant_id` to SQL
- Tables to skip tenant filtering are configured in `application.yml` under `tenant.ignoreTables` and `tenant.ignoreTablePrefix`
- `TenantInterceptor` (Spring MVC) sets tenant context before controllers; clears in `afterCompletion`

### Authentication & Authorization
- Stateless JWT auth (no sessions). Token stored in Redis keyed by user type (`sys_login_info:`, `member_login_info:`, `companion_login_info:`)
- `JwtAuthenticationTokenFilter` extracts Bearer token → parses JWT → loads `LoginUserInfo` from Redis → sets `SecurityContextHolder`
- White-listed URLs configured in `application.yml` under `security.white.url`
- Method-level auth via `@PreAuthorize("hasAuthority('...')")` — permission strings like `sys:user:page`
- `SecurityUtil` provides static helpers: `getUserId()`, `getUsername()`, `getUserInfo()`, `isAdmin()`

### Multi-User Type System
- `UserTypeEnum`: PLATFORM (platform admin), MEMBER (member), COMPANION (companion)
- `LoginUserInfo` wraps `BaseUserInfo` (polymorphic) + permissions + `DataPermission`
- `MultiUserDetailsServiceImpl` routes to correct `UserDetailsService` by user type
- Platform users use `SysUser` entity; other types use their own `PlatformUserInfo` subclasses

### Data Permission
- `@DataScope` annotation on mapper methods triggers `MyDataPermissionInterceptor`
- `DataPermission` model carries userId, deptId, roleIds, and scope types (ALL, DEPT, SELF, CUSTOM)

### Key Utilities
- `SecurityUtil` — get current user info from SecurityContext
- `JwtUtil` — generate/parse JWT tokens, get Redis key by user type
- `RedisUtil` — Redis operations wrapper
- `TreeUtil` / `TreeNode` — build tree structures from flat lists
- `ExcelUtils` — Excel export via EasyExcel
- `OssUtil` — file upload to cloud storage
- `CryptoUtil` — request/response encryption/decryption

## Conventions

- Entity primary keys use Snowflake ID (`assign_id` strategy) — type `Long`
- `BaseEntity` provides `creator`, `createTime`, `updater`, `updateTime` (auto-filled by `MyMetaObjectHandler`)
- Soft delete pattern: `deleted` field (0 = active, 1 = deleted)
- Lombok `@Data` on all entities, params, and VOs
- API documentation via Knife4j/Swagger (`@Tag`, `@Operation`, `@ApiOperationSupport`)
- Operation logging via `@Log(module, type, description)` AOP annotation
- Mapper XML files located at `zs-modules/*/service/src/main/resources/mapper/**/*.xml`
- Module API packages (e.g., `zs-system-api`) have no `com.zs` prefix in their package path — they use bare `config.dto`, etc.

## Database

- MySQL 9.x with Druid connection pool
- Init script: `db/zs_admin.sql`
- Quartz uses JDBC job store (`spring.quartz.job-store-type=jdbc`, schema auto-init disabled)
