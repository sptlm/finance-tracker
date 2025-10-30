<#include "base.ftl">

<#macro title>Теги - Финансовый Трекер</#macro>

<#macro content>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1><i class="bi bi-tags"></i> Теги</h1>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">
            <i class="bi bi-plus-circle"></i> Добавить тег
        </button>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <#if tags?has_content>
                <div class="d-flex flex-wrap gap-2">
                    <#list tags as tag>
                        <div class="badge fs-6 d-inline-flex align-items-center"
                             style="background-color: ${tag.color!'#6c757d'};">
                            <span class="me-2">${tag.name}</span>
                            <button class="btn btn-sm p-0 border-0 text-white"
                                    onclick="editTag(${tag.id}, '${tag.name}', '${tag.color!'#6c757d'}')"
                                    style="background: none;">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <form method="post" action="${Application.contextPath}/tags"
                                  style="display:inline;" onsubmit="return confirm('Удалить тег?')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${tag.id}">
                                <button type="submit" class="btn btn-sm p-0 ms-1 border-0 text-white"
                                        style="background: none;">
                                    <i class="bi bi-x-circle"></i>
                                </button>
                            </form>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="text-center py-5">
                    <i class="bi bi-tags text-muted" style="font-size: 4rem;"></i>
                    <h4 class="mt-3">У вас пока нет тегов</h4>
                    <p class="text-muted">Создайте теги для организации транзакций</p>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">
                        <i class="bi bi-plus-circle"></i> Создать тег
                    </button>
                </div>
            </#if>
        </div>
    </div>

    <!-- Модальное окно создания -->
    <div class="modal fade" id="createModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" action="${Application.contextPath}/tags">
                    <div class="modal-header">
                        <h5 class="modal-title">Создать тег</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create">

                        <div class="mb-3">
                            <label for="name" class="form-label">Название тега</label>
                            <input type="text" class="form-control" id="name" name="name"
                                   required placeholder="Например: Важное">
                        </div>

                        <div class="mb-3">
                            <label for="color" class="form-label">Цвет тега</label>
                            <div class="d-flex align-items-center gap-2">
                                <input type="color" class="form-control form-control-color"
                                       id="color" name="color" value="#0d6efd">
                                <span class="badge fs-6" id="colorPreview"
                                      style="background-color: #0d6efd; padding: 8px 16px;">
                                    Превью тега
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary">Создать</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Модальное окно редактирования -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" action="${Application.contextPath}/tags">
                    <div class="modal-header">
                        <h5 class="modal-title">Редактировать тег</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" id="editId">

                        <div class="mb-3">
                            <label for="editName" class="form-label">Название тега</label>
                            <input type="text" class="form-control" id="editName" name="name" required>
                        </div>

                        <div class="mb-3">
                            <label for="editColor" class="form-label">Цвет тега</label>
                            <div class="d-flex align-items-center gap-2">
                                <input type="color" class="form-control form-control-color"
                                       id="editColor" name="color">
                                <span class="badge fs-6" id="editColorPreview"
                                      style="padding: 8px 16px;">
                                    Превью тега
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Обновление превью цвета при создании
        document.getElementById('color').addEventListener('input', function() {
            document.getElementById('colorPreview').style.backgroundColor = this.value;
        });

        // Обновление превью цвета при редактировании
        document.getElementById('editColor').addEventListener('input', function() {
            document.getElementById('editColorPreview').style.backgroundColor = this.value;
        });

        function editTag(id, name, color) {
            document.getElementById('editId').value = id;
            document.getElementById('editName').value = name;
            document.getElementById('editColor').value = color;
            document.getElementById('editColorPreview').style.backgroundColor = color;
            document.getElementById('editColorPreview').textContent = name;

            const editModal = new bootstrap.Modal(document.getElementById('editModal'));
            editModal.show();
        }
    </script>
</#macro>