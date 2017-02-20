angular.module('JamStash')
.controller('AppController', ['$scope', '$rootScope', '$document', '$window', '$location', '$cookieStore', '$http', 'utils', 'globals', 'model', 'notifications', 'player', 'persistence', 'Page','$timeout',
    function($scope, $rootScope, $document, $window, $location, $cookieStore, $http, utils, globals, model, notifications, player, persistence, Page, $timeout) {
    'use strict';

    $rootScope.settings = globals.settings;
    $rootScope.song = [];
    $rootScope.playingSong = null;
    $rootScope.MusicFolders = [];
    $rootScope.Genres = [];
    $rootScope.Messages = [];

    $rootScope.SelectedMusicFolder = "newest";
    $rootScope.unity = null;
    $scope.Page = Page;
    $rootScope.loggedIn = function () {
        if (globals.settings.Server !== '' && globals.settings.Username !== '' && globals.settings.Password !== '') {
        	persistence.saveSettings($rootScope.settings);
            return true;
        } else {
            return false;
        }
    };
    $rootScope.totalDisplayed = 50;
    $rootScope.loadMore = function () {
        $scope.totalDisplayed += 50;
    };
    $rootScope.go = function (path) {
        $location.path(path);
    };

    // Reads cookies and sets globals.settings values
    $scope.loadSettings = function () {
        // Temporary Code to Convert Cookies added 2/2/2014
        if ($cookieStore.get('Settings')) {
            persistence.saveSettings($cookieStore.get('Settings'));
            persistence.saveSettings($rootScope.getSettings());
            $cookieStore.remove('Settings');
        }
        var settings = persistence.getSettings();
        if (settings !== undefined) {
            //var updSettings = _(settings).omit('Url');
            // We can't just assign settings to globals.settings because it's on the scope
            // TODO: Hyz: remove $rootScope.settings and replace with individual settings
            //_(updSettings).each(function(val, key) {
            //    globals.settings[key] = val;
        	persistence.saveSettings(globals.settings);
           // });
        }
        if (utils.getValue("SavedCollections")) { globals.SavedCollections = utils.getValue("SavedCollections").split(","); }
        if (utils.getValue("DefaultCollection")) { globals.DefaultCollection = utils.getValue("DefaultCollection"); }
        if (utils.getValue("SavedGenres")) { globals.SavedGenres = utils.getValue("SavedGenres").split(","); }
        if (globals.settings.Debug) { console.log('Loaded Settings: ' + JSON.stringify(globals.settings, null, 2)); }
    };
    $scope.toggleSetting = function (setting) {
        var id = setting;
        if (globals.settings[id]) {
            globals.settings[id] = false;
        } else {
            globals.settings[id] = true;
        }
       // notifications.updateMessage(setting + ' : ' + globals.settings[id], true);
    };

    $.ajaxSetup({
        'beforeSend': function () {
            $("#loading").show();
        },
        'complete': function () {
            $("#loading").hide();
        }
    });

    var submenu_active = false;
    $('div.submenu').mouseenter(function () {
        submenu_active = true;
    });
    $('div.submenu').mouseleave(function () {
        submenu_active = false;
        $('div.submenu').hide();
        //setTimeout(function () { if (submenu_active == false) $('div.submenu').stop().fadeOut(); }, 400);
    });
    $scope.toggleSubmenu = function (menu, pl, pos, margin) {
        var submenu = $(menu);
        if (submenu.css('display') !== 'none') {
            submenu.fadeOut();
        } else {
            var el = $(pl);
            var off = el.offset();
            var width = el.width();
            var height = el.height();
            switch (pos) {
                case 'right':
                    //show the menu to the right of placeholder
                    submenu.css({ "left": (off.left + margin) + "px", "top": (off.top) + "px" }).fadeIn(400);
                    break;
                case 'left':
                    //show the menu to the right of placeholder
                    submenu.css({ "left": (off.left - margin) + "px", "top": (off.top) + "px" }).fadeIn(400);
                    break;
            }
            setTimeout(function () { if (submenu_active === false) $('div.submenu').stop().fadeOut(); }, 10000);
        }
    };

    $scope.$watchCollection(function () {
        return player.queue;
    }, function(newQueue) {
        if (newQueue !== undefined && newQueue.length > 0 && globals.settings.ShowQueue) {
            $scope.showQueue();
        }
    });

    $rootScope.showQueue = function () {
        $('#SideBar').css('display', 'block');
       /* $('#right-component').removeClass('lgcolumn_expanded');*/
    };
    $rootScope.hideQueue = function () {
        $('#SideBar').css('display', 'none');
      /*  $('#right-component').addClass('lgcolumn_expanded');*/
    };
    $scope.toggleQueue = function () {
        if ($('#SideBar').css('display') === 'none') {
            $rootScope.showQueue();
        } else {
            $rootScope.hideQueue();
        }
    };
    $scope.showQueue = function () {
        
            $rootScope.showQueue();
   
    };
    $rootScope.showArtists = function () {
        $('#left-component').css('display', '');
        $('#right-component').removeClass('lgcolumn_expandedleft');
    };
    $rootScope.hideArtists = function () {
        $('#left-component').css('display', 'none');
        $('#right-component').addClass('lgcolumn_expandedleft');
    };
    $scope.toggleArtists = function () {
        if ($('#left-component').css('display') == 'none') {
            $rootScope.showArtists();
        } else {
            $rootScope.hideArtists();
        }
    };

    $scope.fancyboxOpenImage = function (url) {
        $.fancybox.open({
            helpers : {
                overlay : {
                    css : {
                        'background' : 'rgba(0, 0, 0, 0.15)'
                    }
                }
            },
            hideOnContentClick: true,
            type: 'image',
            openEffect: 'none',
            closeEffect: 'none',
            href: url
        });
    };

	$(document).on("click", ".message", function(){
	   $(this).remove();
	});

    // Global Functions
    window.onbeforeunload = function () {
        if (!globals.settings.Debug) {
            if (player.queue.length > 0) {
                return "You're about to end your session, are you sure?";
            }
        }
    };
    $rootScope.showIndex = false;
    $(document).on( 'click', 'message', function() {
        $(this).fadeOut(function () { $(this).remove(); });
        return false;
    })
    $document.keydown(function (e) {
        $scope.scrollToIndex(e);
    });
    $scope.scrollToIndex = function (e) {
        var source = e.target.id;
        if (e.target.tagName.toUpperCase() != 'INPUT') {
            var unicode = e.charCode ? e.charCode : e.keyCode;
            if (globals.settings.Debug) { console.log('Keycode Triggered: ' + unicode); }
            if (unicode == 49) { // 1
                $('#action_Queue').click();
            } else if (unicode == 50) {
                $('#action_Library').click();
            } else if (unicode == 51) {
                $('#action_Archive').click();
            } else if (unicode == 52) {
                $('#action_Settings').click();
            } else if (unicode == 53) {
            } else if (unicode == 54) { // 6
            }
            if (unicode >= 65 && unicode <= 90 && $('#tabLibrary').is(':visible')) { // a-z
                var key = utils.findKeyForCode(unicode);
                if (key == 'x' || key == 'y' || key == 'z') {
                    key = 'x-z';
                }
                var el = '#' + key.toUpperCase();
                if ($(el).length > 0) {
                    $('#left-component').stop().scrollTo(el, 400);
                }
            } else if (unicode == 36 && $('#tabLibrary').is(':visible')) { // home
                $('#left-component').stop().scrollTo('#MusicFolders', 400);
            }
        }
        return true;
    };
    $scope.scrollToIndexName = function (index) {
        var el = '#' + index;
        if ($(el).length > 0) {
            $('#left-component').stop().scrollTo(el, 400);
        }
    };
    $scope.scrollToTop = function () {
        $('#left-component').stop().scrollTo('#MusicFolders', 400);
    };
    $rootScope.selectAll = function (songs) {
        angular.forEach(songs, function (item, key) {
            $scope.selectedSongs.push(item);
            item.selected = true;
        });
    };
    $rootScope.selectNone = function (songs) {
        angular.forEach(songs, function (item, key) {
            $scope.selectedSongs = [];
            item.selected = false;
        });
    };
    $rootScope.playAll = function (songs) {
        // TODO: Hyz: Replace
        player.queue = [];
        $rootScope.selectAll(songs);
        $rootScope.addSongsToQueue();
        var next = player.queue[0];
        player.play(next);
    };
    $rootScope.playFrom = function (index, songs) {
        // TODO: Hyz: Replace
        var from = songs.slice(index,songs.length);
        $scope.selectedSongs = [];
        angular.forEach(from, function (item, key) {
            $scope.selectedSongs.push(item);
            item.selected = true;
        });
        if ($scope.selectedSongs.length > 0) {
            player.queue = [];
            $rootScope.addSongsToQueue();
            var next = player.queue[0];
            player.play(next);
        }
    };
    
    /* ToDo
     * 
     * $rootScope.playFrom = function (index, songs) {
        // TODO: Hyz: Replace
    	if ($scope.selectedSongs.length = 0) {
        var from = songs.slice(index,index+1);
        $scope.selectedSongs = [];
        angular.forEach(from, function (item, key) {
            $scope.selectedSongs.push(item);
            item.selected = true;
        });
    	}
    	else if ($scope.selectedSongs.length > 0) {
        	if(player.queue.length==0){
        		player.queue = [];
        		$rootScope.addSongsToQueue();
        		var next = player.queue[0];
        		player.play(next);
        	}else{
        		 angular.forEach($scope.selectedSongs, function (item, key) {
                     player.queue.push(item);
                     item.selected = false;
                 });
                 notifications.updateMessage($scope.selectedSongs.length + ' Song(s) Added to Queue', true);
                 $scope.selectedSongs.length = 0;
        	}
        }
    };
     */
    $rootScope.addSongsToQueue = function () {
        // TODO: Hyz: Replace
        if ($scope.selectedSongs.length !== 0) {
            angular.forEach($scope.selectedSongs, function (item, key) {
                player.queue.push(item);
                item.selected = false;
            });
            notifications.updateMessage($scope.selectedSongs.length + ' Song(s) Added to Queue', true);
            $scope.selectedSongs.length = 0;
        }
    };
    $rootScope.removeSong = function (item, songs) {
        // TODO: Hyz: Replace
        var index = songs.indexOf(item);
        songs.splice(index, 1);
    };
    $scope.isActive = function (route) {
        return route === $location.path();
    };
    $rootScope.getSplitPosition = function (scope, elm) {
        window.alert(elm.getBoundingClientRect().left);
    };
    $scope.download = function (id, counter) {
    	
    	/*if($rootScope.user.email==undefined||$rootScope.user.email==''||$rootScope.user.email==null)
		 {
		 alert("Please Login to continue");
		 return false;	
		 
		 }*/
    	
        $.ajax({
            url: globals.BaseURL() + '/getUser.view?' + globals.BaseParams() + '&username=' + globals.settings.Username,
            method: 'GET',
            dataType: globals.settings.Protocol,
            timeout: globals.settings.Timeout,
            success: function (data) {
                if (typeof data["subsonic-response"].error != 'undefined') {
                   // notifications.updateMessage('Error: ' + data["subsonic-response"].error.message, true);
                } else {
                    if (data["subsonic-response"].user.downloadRole === true) {
                    	
                        $timeout(function(){
                        	$window.location.href = globals.BaseURL() + '/download.view?' + globals.BaseParams() + '&id=' + id;},counter*2000);
                        
                    } else {
                        notifications.updateMessage('You do not have permission to Download', true);
                    }
                }
            }
        });
    };
    $scope.selectedSongs = [];
    $scope.selectSong = function (data) {
        var i = $scope.selectedSongs.indexOf(data);
        if (i >= 0) {
            $scope.selectedSongs.splice(i, 1);
            data.selected = false;
        } else {
            $scope.selectedSongs.push(data);
            data.selected = true;
        }
    };
    
    $scope.goToPayment = function (price,songObject) {
    	/*if($rootScope.user.email==undefined||$rootScope.user.email==''||$rootScope.user.email==null)
		 {
		 alert("Please Login to continue");
		 return false;
		 
		 }*/
    	//alert(songObject)
    	$('#spinner').show();
    	var realPrice=price/10;
    	var email=$rootScope.user.email;
    	var data = 'totalPrice='+realPrice ;
   	 $http({
   		  method: 'GET',
   		  url: 'getToken?'+data,
   		  params: {'itemsdetails':songObject.id, 'email':email,'songName':songObject.name,'artist':songObject.artist}
   		}).then(function successCallback(response) {
   			console.log(response);
   			var token = response.data;
   		
   			window.location.href="https://secure.3gdirectpay.com/pay.asp?ID="+token;
   		    
   		  }, function errorCallback(erresponse) {
   			  console.log(erresponse);
   		    // called asynchronously if an error occurs
   		    // or server returns response with an error status.
   		  });
   	
    }
    

    /**
     * Returns true if the target of this event is an input
     * @param  {jQuery event}  event
     * @return {Boolean}
     */
    function isTargetInput (event) {
        return (event && event.target.tagName === "INPUT");
    }

    /* We define player-related methods here instead of in player controller
        in order to bind keypresses to <body> and have global shortcuts.
        We also check the event so we don't do anything if it's on an input */
    $scope.togglePause = function (event) {
        if(!isTargetInput(event)) {
            if(globals.settings.Jukebox) {
                $scope.sendToJukebox('stop');
            } else {
                player.togglePause();
            }
        }
    };

    $scope.turnVolumeUp = function (event) {
        if(!isTargetInput(event)) {
            var volume = player.turnVolumeUp();
            persistence.saveVolume(volume);
        }
    };

    $scope.turnVolumeDown = function (event) {
        if(!isTargetInput(event)) {
            var volume = player.turnVolumeDown();
            persistence.saveVolume(volume);
        }
    };

    $scope.nextTrack = function (event) {
        if(!isTargetInput(event)) {
            player.nextTrack();
        }
    };
    $scope.previousTrack = function (event) {
        if(!isTargetInput(event)) {
            player.previousTrack();
        }
    };

	$rootScope.addToJukebox = function (id) {
		if (globals.settings.Debug) { console.log("LOAD JUKEBOX"); }
		$.ajax({
			url: globals.BaseURL() + '/jukeboxControl.view?' + globals.BaseParams() + '&action=set&id=' + id,
			method: 'GET',
			dataType: globals.settings.Protocol,
			timeout: globals.settings.Timeout,
			success: function (data) {
				/*
				if (data["subsonic-response"].podcasts.channel !== undefined) {
				}
				deferred.resolve(podcasts);
				*/
				$.get(globals.BaseURL() + '/jukeboxControl.view?' + globals.BaseParams() + '&action=start');
			}
		});
	};
	$rootScope.sendToJukebox = function (action) {
		if (globals.settings.Debug) { console.log("SEND JUKEBOX " + action); }
		$.ajax({
			url: globals.BaseURL() + '/jukeboxControl.view?' + globals.BaseParams() + '&action=' + action,
			method: 'GET',
			dataType: globals.settings.Protocol,
			timeout: globals.settings.Timeout,
			success: function (data) {
			}
		});
	};

    $scope.updateFavorite = function (item) {
        var id = item.id;
        var starred = item.starred;
        var url;
        if (starred) {
            url = globals.BaseURL() + '/unstar.view?' + globals.BaseParams() + '&id=' + id;
            item.starred = undefined;
        } else {
            url = globals.BaseURL() + '/star.view?' + globals.BaseParams() + '&id=' + id;
            item.starred = true;
        }
        $.ajax({
            url: url,
            method: 'GET',
            dataType: globals.settings.Protocol,
            timeout: globals.settings.Timeout,
            success: function () {
                notifications.updateMessage('Favorite Updated!', true);
            }
        });
    };
    $scope.toTrusted = function (html) {
        return $sce.trustAsHtml(html);
    };
    
    //save users
    
    $scope.saveUser=function (response) {
    	$rootScope.user.name = response.name;
    	$rootScope.user.id = response.id;
		//alert(globals.user.name);
    	if(response.email==undefined||response.email==''||response.email==null){
    		
    		$rootScope.user.email = response.id+"@facebook.com";
        }else{
        	$rootScope.user.email = response.email;
        }
    	$rootScope.user.firstName = response.first_name;
    	$rootScope.user.lastName = response.last_name;
    	$rootScope.user.gender = response.gender;
    	$rootScope.user.birthday =response.birthday;
    	$rootScope.user.location = response.location ? response.location.name
				: '';
    	$rootScope.user.hometown = response.hometown ? response.hometown.name
				: '';
		//document.forms[0].bio.value = response.bio;
    	$rootScope.user.relationship = response.relationship_status;
    	$rootScope.user.timezone = response.timezone;
    	$rootScope.user.userType = 'U';
    	$rootScope.user.providerId = "1";
    	$rootScope.user.provider = "FB";
    	
    	
			$.ajax({
		        url: "http://m.kalimbaradio.com/setUserDb?email_id="+$rootScope.user.email+"&name="+$rootScope.user.name+"&first_name="+$rootScope.user.firstName+"&last_name="+$rootScope.user.lastName+"&gender="+$rootScope.user.gender+"&birthday="+$rootScope.user.birthday+"&location="+$rootScope.user.location+"&hometown="+$rootScope.user.hometown+"&relationship="+$rootScope.user.relationship+"&timezone="+$rootScope.user.timezone+"&provider="+$rootScope.user.provider+"&provider_id="+$rootScope.user.providerId+"&user_type="+$rootScope.user.userType
		    }).then(function(data) {
		       //alert(data);
		       if(data==='success')
		    	   {
		    	//  alert("SSSSS");
		    	   }
		    });
		
    };
    
    /* Launch on Startup */
    $scope.loadSettings();
    
    utils.switchTheme(globals.settings.Theme);
    $scope.showQueue();

    /*if(!globals.settings.ShowQueue) {
        $rootScope.hideQueue();
    }*/

    if ($scope.loggedIn()) {
        //$scope.ping();
        if (globals.settings.SaveTrackPosition) {
            persistence.loadQueue();
            persistence.loadTrackPosition();
        }
        player.setVolume(persistence.getVolume());
    }
    /* End Startup */
}]);
