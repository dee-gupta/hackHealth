from django.shortcuts import render, get_object_or_404
from django.http import HttpResponseRedirect
from django.views import generic
from django.http import JsonResponse
from myapp import get_value
import predictor
def sampleHtml(request):
	context = ''
	return render(request, 'index.html')

def sampleJson(request):
	return JsonResponse({'foo':'bar'})


def index(request):

    username = request.GET.get('name','')
    response = predictor.getUserTweets(username)
    dict = {'name' : response[0],
            'positive': response[1],
            'negative': response[2],
            'score' :response[3]
            }
    return dict
    
