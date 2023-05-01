<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<%@ include file="head.jsp"%>
<%@ include file="nav.jsp"%>

<script>
	$(document).ready(function() {
		$('.deleteTaskButton').click(function() {
			var taskId = $(this).data('taskid');
			$.ajax({
				url: '${rootDirectory}/deletetask?id=' + taskId,
				type: 'POST',
				success: function() {
					$('#task_' + taskId).remove();
				},
				error: function() {
					alert('Failed to delete the task.');
				}
			});
		});
	});
</script>

<body>
	<div class="container mt-3">
	  <div id="list-view">
	    <div class="table-responsive">
	      <table class="table table-striped">
	        <thead>
	          <tr>
	            <th>ID</th>
	            <th>Title</th>
	            <th>Description</th>
	            <th>Deadline</th>
	            <th>Completed</th>
	            <th>Created At</th>
	            <th>Updated At</th>
	            <th>Delete</th>
	          </tr>
	        </thead>
	        <tbody>
				<c:forEach var="task" items="${tasks}">
					<tr id="task_${task.id}">
						<td>${task.id}</td>
						<td>${task.title}</td>
						<td>${task.description}</td>
						<td><fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd HH:mm:ss.sss"/></td>
						<td>${task.completed}</td>
						<td><fmt:formatDate value="${task.createdAt}" pattern="yyyy-MM-dd HH:mm:ss.sss"/></td>
						<td><fmt:formatDate value="${task.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss.sss"/></td>
						<td>
							<button type="button" class="btn btn-danger deleteTaskButton" data-taskid="${task.id}">Delete</button>
						</td>
					</tr>
				</c:forEach>
	        </tbody>
	      </table>
	    </div>
	  </div>
	</div>
</body>

<%@ include file="footer.jsp"%>

</html>
