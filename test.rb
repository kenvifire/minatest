require 'socket'

count = 100;
hostname = '182.92.77.50';
port =9123 ;

clients = []
startTime = Time.new;
1000.times do  |i|
	clients[i] = Thread.new{
		count = 1000;
		while count > 0
			client = TCPSocket.open(hostname,port)
			
			client.puts "\n"
			sleep 0.01
			client.puts "quit"
			puts client.gets
			client.close
			count = count-1
		end
	}
end

clients.each{ |t| t.join}

endTime = Time.new

puts (endTime - startTime);


