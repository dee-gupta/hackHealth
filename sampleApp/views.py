from django.shortcuts import render, get_object_or_404
from django.http import HttpResponseRedirect
from django.views import generic
from django.http import JsonResponse
from myapp import get_value
def sampleHtml(request):
	context = ''
	return render(request, 'index.html')

def sampleJson(request):
	return JsonResponse({'foo':'bar'})


def index(request):
    data = {
        'name': 'Deepak',
        'location': 'Stony',
        'is_active': True,
        'count': 25,
        'value_a': get_value()
    }

    return JsonResponse(data)
    